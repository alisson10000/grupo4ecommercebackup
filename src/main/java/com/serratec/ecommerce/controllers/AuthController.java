package com.serratec.ecommerce.controllers;

import com.serratec.ecommerce.dtos.LoginDTO;
import com.serratec.ecommerce.securitys.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Gerencia o login e geração de token JWT para acesso às rotas protegidas")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Operation(
        summary = "Realiza login e gera token JWT",
        description = "Autentica o usuário com e-mail e senha. Se as credenciais forem válidas, retorna um token JWT que deve ser usado nas requisições protegidas (via cabeçalho Authorization: Bearer)."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso e token JWT retornado",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(example = "{\"token\": \"jwt_aqui\"}"))),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(example = "\"Credenciais inválidas\"")))
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody(
                description = "Dados de login do usuário (e-mail e senha)",
                required = true,
                content = @Content(schema = @Schema(implementation = LoginDTO.class))
            )
            @org.springframework.web.bind.annotation.RequestBody LoginDTO loginDTO) {

        try {
            // Apenas autentica — se inválido, lança exceção automaticamente
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(), loginDTO.getPassword())
            );

            String token = jwtUtil.generateToken(loginDTO.getEmail());
            return ResponseEntity.ok().body("{\"token\": \"" + token + "\"}");

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }
}
