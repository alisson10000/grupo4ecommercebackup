package com.serratec.ecommerce.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.serratec.ecommerce.dtos.ProdutoDTO;
import com.serratec.ecommerce.dtos.ProdutoInserirDTO;
import com.serratec.ecommerce.services.ProdutoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
@Validated
@Tag(name = "Produtos", description = "CRUD de produtos e consultas auxiliares")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // LISTAR TODOS
    @GetMapping
    @Operation(summary = "Lista todos os produtos")
    public ResponseEntity<List<ProdutoDTO>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    @Operation(summary = "Busca um produto por ID")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    // INSERIR
    @PostMapping
    @Operation(
            summary = "Cadastra um novo produto",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Produto criado",
                            content = @Content(schema = @Schema(implementation = ProdutoDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Validação/Regra de negócio violada")
            }
    )
    public ResponseEntity<ProdutoDTO> inserir(
            @Valid @RequestBody ProdutoInserirDTO dto,
            UriComponentsBuilder uriBuilder) {

        ProdutoDTO salvo = produtoService.inserir(dto);
        URI uri = uriBuilder.path("/produtos/{id}").buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(uri).body(salvo);
    }

    // ATUALIZAR (PUT)
    @PutMapping("/{id}")
    @Operation(
            summary = "Atualiza um produto existente (substituição dos campos enviados)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Produto atualizado",
                            content = @Content(schema = @Schema(implementation = ProdutoDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
            }
    )
    public ResponseEntity<ProdutoDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoInserirDTO dto) {

        return ResponseEntity.ok(produtoService.atualizar(id, dto));
    }

    // DELETAR
    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um produto por ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // AJUSTAR ESTOQUE (PATCH)
    @PatchMapping("/{id}/estoque")
    @Operation(summary = "Ajusta o estoque (ex.: +5 ou -3) via query param 'ajuste'")
    public ResponseEntity<ProdutoDTO> ajustarEstoque(
            @PathVariable Long id,
            @RequestParam Integer ajuste) {

        return ResponseEntity.ok(produtoService.ajustarEstoque(id, ajuste));
    }

    // --- Consultas auxiliares (opcionais) ---

    @GetMapping("/search")
    @Operation(summary = "Busca produtos pelo nome (contendo, case-insensitive)")
    public ResponseEntity<List<ProdutoDTO>> buscarPorNome(
            @RequestParam String nome) {

        return ResponseEntity.ok(produtoService.buscarPorNome(nome));
    }

    @GetMapping("/categoria/{categoriaId}")
    @Operation(summary = "Lista produtos por categoria")
    public ResponseEntity<List<ProdutoDTO>> buscarPorCategoria(
            @PathVariable Long categoriaId) {

        return ResponseEntity.ok(produtoService.buscarPorCategoria(categoriaId));
    }

    @GetMapping("/estoque/abaixo")
    @Operation(summary = "Lista produtos com estoque abaixo do valor informado")
    public ResponseEntity<List<ProdutoDTO>> comEstoqueAbaixoDe(
            @RequestParam Integer quantidade) {

        return ResponseEntity.ok(produtoService.comEstoqueAbaixoDe(quantidade));
    }

    @GetMapping("/preco/ate")
    @Operation(summary = "Lista produtos com preço menor que o valor informado")
    public ResponseEntity<List<ProdutoDTO>> comPrecoAbaixoDe(
            @RequestParam Double valor) {

        return ResponseEntity.ok(produtoService.comPrecoAbaixoDe(valor));
    }
}
