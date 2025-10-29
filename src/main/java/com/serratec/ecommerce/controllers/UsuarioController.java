package com.serratec.ecommerce.controllers;

import java.util.List;

import com.serratec.ecommerce.dtos.UsuarioDTO;
import com.serratec.ecommerce.dtos.UsuarioInserirDTO;
import com.serratec.ecommerce.services.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Gerencia os usuários do sistema (ADMIN e USER)")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // 🔹 Criar novo usuário
    @Operation(summary = "Cria um novo usuário", description = "Endpoint para cadastrar um novo usuário no sistema. Requer autenticação ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @PostMapping
    public ResponseEntity<UsuarioDTO> criar(
            @Parameter(description = "Dados do novo usuário a ser cadastrado") 
            @Valid @RequestBody UsuarioInserirDTO dto) {

        UsuarioDTO novo = usuarioService.criar(dto);
        return ResponseEntity.ok(novo);
    }

    // 🔹 Listar todos
    @Operation(summary = "Lista todos os usuários", description = "Retorna a lista completa de usuários cadastrados. Apenas ADMIN pode acessar.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    // 🔹 Buscar por ID
    @Operation(summary = "Busca usuário por ID", description = "Retorna um usuário específico a partir do seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(
            @Parameter(description = "ID do usuário a ser buscado") 
            @PathVariable Long id) {

        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    // 🔹 Atualizar
    @Operation(summary = "Atualiza um usuário existente", description = "Permite atualizar informações de um usuário existente. Requer autenticação ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizar(
            @Parameter(description = "ID do usuário que será atualizado") 
            @PathVariable Long id,
            @Valid @RequestBody UsuarioInserirDTO dto) {

        return ResponseEntity.ok(usuarioService.atualizar(id, dto));
    }

    // 🔹 Deletar
    @Operation(summary = "Remove um usuário", description = "Exclui permanentemente um usuário pelo ID. Requer autenticação ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do usuário que será removido") 
            @PathVariable Long id) {

        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
