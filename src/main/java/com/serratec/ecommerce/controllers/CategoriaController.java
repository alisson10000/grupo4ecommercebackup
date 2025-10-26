package com.serratec.ecommerce.controllers;

import com.serratec.ecommerce.dtos.CategoriaDTO;
import com.serratec.ecommerce.services.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@Tag(name = "Categorias", description = "Gerencia as categorias de produtos do sistema")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // 🔹 GET – Listar todas as categorias
    @Operation(
        summary = "Lista todas as categorias",
        description = "Retorna uma lista com todas as categorias cadastradas."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categorias listadas com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listarTodas() {
        List<CategoriaDTO> categorias = categoriaService.listarTodas();
        return ResponseEntity.ok(categorias);
    }

    // 🔹 GET – Buscar categoria por ID
    @Operation(
        summary = "Busca uma categoria pelo ID",
        description = "Retorna os dados de uma categoria específica, caso exista."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> buscarPorId(
            @Parameter(description = "ID da categoria a ser buscada", example = "1")
            @PathVariable Long id) {

        CategoriaDTO categoria = categoriaService.buscarPorId(id);
        return ResponseEntity.ok(categoria);
    }

    // 🔹 POST – Criar nova categoria
    @Operation(
        summary = "Cria uma nova categoria",
        description = "Cria uma nova categoria de produto. Apenas usuários com perfil ADMIN podem executar esta operação."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoria criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @PostMapping
    public ResponseEntity<CategoriaDTO> criar(
            @Parameter(description = "Dados da categoria a ser criada") 
            @Valid @RequestBody CategoriaDTO dto) {

        CategoriaDTO nova = categoriaService.criar(dto);
        return ResponseEntity.ok(nova);
    }

    // 🔹 PUT – Atualizar categoria existente
    @Operation(
        summary = "Atualiza uma categoria existente",
        description = "Atualiza os dados de uma categoria com base no ID informado."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> atualizar(
            @Parameter(description = "ID da categoria a ser atualizada", example = "1") 
            @PathVariable Long id,
            @Parameter(description = "Novos dados da categoria") 
            @RequestBody CategoriaDTO dto) {

        CategoriaDTO atualizada = categoriaService.atualizar(id, dto);
        return ResponseEntity.ok(atualizada);
    }

    // 🔹 DELETE – Excluir categoria
    @Operation(
        summary = "Exclui uma categoria",
        description = "Remove uma categoria do sistema pelo seu ID. Operação restrita a administradores."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID da categoria a ser excluída", example = "1") 
            @PathVariable Long id) {

        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
