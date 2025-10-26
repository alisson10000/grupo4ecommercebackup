package com.serratec.ecommerce.controllers;

import com.serratec.ecommerce.dtos.AtualizarStatusDTO;
import com.serratec.ecommerce.dtos.PedidoRequestDTO;
import com.serratec.ecommerce.dtos.PedidoResponseDTO;

import com.serratec.ecommerce.services.PedidoService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "Gerenciamento de pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @Operation(summary = "Criar novo pedido")
    public ResponseEntity<PedidoResponseDTO> criarPedido(@Valid @RequestBody PedidoRequestDTO pedidoRequestDTO) {
        PedidoResponseDTO novoPedido = pedidoService.criarPedido(pedidoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID com total")
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Long id) {
        PedidoResponseDTO pedido = pedidoService.buscarPorId(id);
        return ResponseEntity.ok(pedido);
    }

    @GetMapping
    @Operation(summary = "Listar todos os pedidos")
    public ResponseEntity<List<PedidoResponseDTO>> listarTodos() {
        List<PedidoResponseDTO> pedidos = pedidoService.listarTodos();
        return ResponseEntity.ok(pedidos);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pedido completo")
    public ResponseEntity<PedidoResponseDTO> atualizarPedido(
            @PathVariable Long id,
            @Valid @RequestBody PedidoRequestDTO dto) {
        PedidoResponseDTO pedidoAtualizado = pedidoService.atualizarPedido(id, dto);
        return ResponseEntity.ok(pedidoAtualizado);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar apenas o status do pedido")
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarStatusDTO dto) {
        PedidoResponseDTO pedidoAtualizado = pedidoService.atualizarStatus(id, dto);
        return ResponseEntity.ok(pedidoAtualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar pedido")
    public ResponseEntity<Void> deletarPedido(@PathVariable Long id) {
        pedidoService.deletarPedido(id);
        return ResponseEntity.noContent().build();
    }
}