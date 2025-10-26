package com.serratec.ecommerce.services;

import com.serratec.ecommerce.dtos.CategoriaDTO;
import com.serratec.ecommerce.entitys.Categoria;
import com.serratec.ecommerce.repositorys.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    //  Listar todas as categorias
    public List<CategoriaDTO> listarTodas() {
        return categoriaRepository.findAll()
                .stream()
                .map(CategoriaDTO::new)
                .collect(Collectors.toList());
    }

    //  Buscar categoria por ID
    public CategoriaDTO buscarPorId(Long id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        return categoria.map(CategoriaDTO::new)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + id));
    }

    //  Criar nova categoria
    public CategoriaDTO criar(CategoriaDTO dto) {
        if (categoriaRepository.findByNome(dto.getNome()).isPresent()) {
            throw new RuntimeException("Já existe uma categoria com esse nome.");
        }

        Categoria nova = dto.toEntity();
        Categoria salva = categoriaRepository.save(nova);
        return new CategoriaDTO(salva);
    }

    //  Atualizar categoria existente
    public CategoriaDTO atualizar(Long id, CategoriaDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + id));

        categoria.setNome(dto.getNome());
        categoria.setDescricao(dto.getDescricao());

        Categoria atualizada = categoriaRepository.save(categoria);
        return new CategoriaDTO(atualizada);
    }

    //  Excluir categoria
    public void deletar(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoria não encontrada com ID: " + id);
        }
        categoriaRepository.deleteById(id);
    }
}
