package com.serratec.ecommerce.services;

import com.serratec.ecommerce.dtos.CategoriaResumoDTO;
import com.serratec.ecommerce.dtos.ProdutoDTO;
import com.serratec.ecommerce.dtos.ProdutoInserirDTO;
import com.serratec.ecommerce.entitys.Categoria;
import com.serratec.ecommerce.entitys.Produto;
import com.serratec.ecommerce.exceptions.BusinessRuleException;
import com.serratec.ecommerce.exceptions.EntityNotFoundException;
import com.serratec.ecommerce.repositorys.CategoriaRepository;
import com.serratec.ecommerce.repositorys.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository,
                          CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // LISTAR TODOS
    @Transactional(readOnly = true)
    public List<ProdutoDTO> listarTodos() {
        return produtoRepository.findAll()
                .stream()
                .map(ProdutoDTO::new)
                .toList();
    }

    // BUSCAR POR ID
    @Transactional(readOnly = true)
    public ProdutoDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado (id=" + id + ")"));
        return new ProdutoDTO(produto);
    }

    // INSERIR
    @Transactional
    public ProdutoDTO inserir(ProdutoInserirDTO dto) {
        // Verifica duplicidade por nome (case-insensitive, com match exato dentro do resultado do "containing")
        boolean nomeJaExiste = produtoRepository.findByNomeContainingIgnoreCase(dto.getNome())
                .stream()
                .anyMatch(p -> p.getNome().equalsIgnoreCase(dto.getNome()));

        if (nomeJaExiste) {
            throw new BusinessRuleException("Já existe produto com o nome: " + dto.getNome());
        }

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada (id=" + dto.getCategoriaId() + ")"));

        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        produto.setQuantidadeEstoque(dto.getQuantidadeEstoque());
        produto.setCategoria(categoria);

        produto = produtoRepository.save(produto);
        return new ProdutoDTO(produto);
    }

    // ATUALIZAR (PUT)
    @Transactional
    public ProdutoDTO atualizar(Long id, ProdutoInserirDTO dto) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado (id=" + id + ")"));

        // Se nome vier e mudou, valida duplicidade (excluindo o próprio id)
        if (dto.getNome() != null && !dto.getNome().equalsIgnoreCase(produto.getNome())) {
            boolean nomeJaUsadoPorOutro = produtoRepository.findByNomeContainingIgnoreCase(dto.getNome())
                    .stream()
                    .anyMatch(p -> !p.getId().equals(id) && p.getNome().equalsIgnoreCase(dto.getNome()));
            if (nomeJaUsadoPorOutro) {
                throw new BusinessRuleException("Já existe produto com o nome: " + dto.getNome());
            }
            produto.setNome(dto.getNome());
        }

        if (dto.getDescricao() != null) produto.setDescricao(dto.getDescricao());
        if (dto.getPreco() != null) produto.setPreco(dto.getPreco());
        if (dto.getQuantidadeEstoque() != null) produto.setQuantidadeEstoque(dto.getQuantidadeEstoque());

        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada (id=" + dto.getCategoriaId() + ")"));
            produto.setCategoria(categoria);
        }

        produto = produtoRepository.save(produto);
        return new ProdutoDTO(produto);
    }

    // DELETAR
    @Transactional
    public void deletar(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado (id=" + id + ")"));
        produtoRepository.delete(produto);
    }

    // AJUSTAR ESTOQUE (ex.: +5 ou -3)
    @Transactional
    public ProdutoDTO ajustarEstoque(Long id, Integer ajuste) {
        if (ajuste == null) {
            throw new BusinessRuleException("Informe o valor de ajuste de estoque.");
        }

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado (id=" + id + ")"));

        int novoEstoque = produto.getQuantidadeEstoque() + ajuste;
        if (novoEstoque < 0) {
            throw new BusinessRuleException("Estoque não pode ficar negativo.");
        }
        produto.setQuantidadeEstoque(novoEstoque);

        produto = produtoRepository.save(produto);
        return new ProdutoDTO(produto);
    }

    // CONSULTAS DE APOIO (opcional, já que você tem os métodos no repository)
    @Transactional(readOnly = true)
    public List<ProdutoDTO> buscarPorNome(String termo) {
        return produtoRepository.findByNomeContainingIgnoreCase(termo)
                .stream()
                .map(ProdutoDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO> buscarPorCategoria(Long categoriaId) {
        return produtoRepository.findByCategoriaId(categoriaId)
                .stream()
                .map(ProdutoDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO> comEstoqueAbaixoDe(Integer quantidade) {
        return produtoRepository.findByQuantidadeEstoqueLessThan(quantidade)
                .stream()
                .map(ProdutoDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO> comPrecoAbaixoDe(Double preco) {
        return produtoRepository.findByPrecoLessThan(preco)
                .stream()
                .map(ProdutoDTO::new)
                .toList();
    }

    // Conversor manual (caso prefira usar em algum ponto)
    private ProdutoDTO toDTO(Produto p) {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(p.getId());
        dto.setNome(p.getNome());
        dto.setDescricao(p.getDescricao());
        dto.setPreco(p.getPreco());
        dto.setQuantidadeEstoque(p.getQuantidadeEstoque());
        if (p.getCategoria() != null) {
            dto.setCategoria(new CategoriaResumoDTO(p.getCategoria().getId(), p.getCategoria().getNome()));
        }
        return dto;
    }
}
