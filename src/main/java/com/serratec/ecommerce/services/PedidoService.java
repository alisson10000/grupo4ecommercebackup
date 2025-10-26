package com.serratec.ecommerce.services;


import com.serratec.ecommerce.dtos.*;
import com.serratec.ecommerce.entitys.Cliente;
import com.serratec.ecommerce.entitys.ItemPedido;
import com.serratec.ecommerce.entitys.Pedido;
import com.serratec.ecommerce.entitys.Produto;
import com.serratec.ecommerce.exceptions.ClienteNaoEncontradoException;
import com.serratec.ecommerce.exceptions.PedidoNaoEncontradoException;
import com.serratec.ecommerce.exceptions.ProdutoNaoEncontradoException;
import com.serratec.ecommerce.repositorys.ClienteRepository;
import com.serratec.ecommerce.repositorys.PedidoRepository;
import com.serratec.ecommerce.repositorys.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoService(PedidoRepository pedidoRepository,
                         ClienteRepository clienteRepository,
                         ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public PedidoResponseDTO criarPedido(PedidoRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ClienteNaoEncontradoException(dto.getClienteId()));

        Pedido pedido = new Pedido(cliente);

        if (dto.getStatus() != null) {
            pedido.setStatus(dto.getStatus());
        }

        for (ItemPedidoDTO itemDto : dto.getItens()) {
            Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                    .orElseThrow(() -> new ProdutoNaoEncontradoException(itemDto.getProdutoId()));

            pedido.adicionarItem(
                    produto,
                    itemDto.getQuantidade(),
                    itemDto.getValorVenda(),
                    itemDto.getDesconto() != null ? itemDto.getDesconto() : BigDecimal.ZERO
            );
        }

        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        return converterParaDTO(pedidoSalvo);
    }

    @Transactional
    public PedidoResponseDTO buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNaoEncontradoException(id));
        return converterParaDTO(pedido);
    }

    @Transactional
    public List<PedidoResponseDTO> listarTodos() {
        return pedidoRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PedidoResponseDTO atualizarPedido(Long id, PedidoRequestDTO dto) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNaoEncontradoException(id));

        if (dto.getClienteId() != null && !dto.getClienteId().equals(pedido.getCliente().getId())) {
            Cliente novoCliente = clienteRepository.findById(dto.getClienteId())
                    .orElseThrow(() -> new ClienteNaoEncontradoException(dto.getClienteId()));
            pedido.setCliente(novoCliente);
        }

        if (dto.getStatus() != null) {
            pedido.setStatus(dto.getStatus());
        }

        if (dto.getItens() != null && !dto.getItens().isEmpty()) {
            pedido.limparItens();

            for (ItemPedidoDTO itemDto : dto.getItens()) {
                Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                        .orElseThrow(() -> new ProdutoNaoEncontradoException(itemDto.getProdutoId()));

                pedido.adicionarItem(
                        produto,
                        itemDto.getQuantidade(),
                        itemDto.getValorVenda(),
                        itemDto.getDesconto() != null ? itemDto.getDesconto() : BigDecimal.ZERO
                );
            }
        }

        Pedido pedidoAtualizado = pedidoRepository.save(pedido);
        return converterParaDTO(pedidoAtualizado);
    }

    @Transactional
    public PedidoResponseDTO atualizarStatus(Long id, AtualizarStatusDTO dto) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNaoEncontradoException(id));

        pedido.setStatus(dto.getStatus());
        Pedido pedidoAtualizado = pedidoRepository.save(pedido);
        return converterParaDTO(pedidoAtualizado);
    }

    @Transactional
    public void deletarPedido(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new PedidoNaoEncontradoException(id);
        }
        pedidoRepository.deleteById(id);
    }

    private PedidoResponseDTO converterParaDTO(Pedido pedido) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(pedido.getId());
        dto.setDataPedido(pedido.getDataPedido());
        dto.setNomeCliente(pedido.getCliente().getNome());
        dto.setStatus(pedido.getStatus());
        dto.setValorTotal(pedido.calcularTotal());

        List<ItemPedidoResponseDTO> itensDto = pedido.getItens().stream()
                .map(this::converterItemParaDTO)
                .collect(Collectors.toList());
        dto.setItens(itensDto);

        return dto;
    }

    private ItemPedidoResponseDTO converterItemParaDTO(ItemPedido item) {
        ItemPedidoResponseDTO dto = new ItemPedidoResponseDTO();
        dto.setId(item.getId());
        dto.setProdutoId(item.getProduto().getId());
        dto.setNomeProduto(item.getProduto().getNome());
        dto.setQuantidade(item.getQuantidade());
        dto.setValorVenda(item.getValorVenda());
        dto.setDesconto(item.getDesconto());
        dto.setSubtotal(item.getSubtotal());
        return dto;
    }
}

