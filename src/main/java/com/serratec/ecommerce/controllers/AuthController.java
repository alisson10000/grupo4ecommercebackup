package com.serratec.ecommerce.controllers;

import com.serratec.ecommerce.dtos.LoginDTO;
import com.serratec.ecommerce.entitys.Usuario;
import com.serratec.ecommerce.repositorys.UsuarioRepository;
import com.serratec.ecommerce.securitys.JwtUtil;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Autenticação", description = "Gerencia o login e geração de token JWT para acesso às rotas protegidas")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository; // 🔹 novo

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
    }

    @Operation(summary = "Realiza login e gera token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@org.springframework.web.bind.annotation.RequestBody LoginDTO loginDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
            );

            // 🔹 Busca o usuário no banco para pegar o nome
            Usuario usuario = usuarioRepository.findByEmail(loginDTO.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

            // 🔹 Gera o token com email + nome
            String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getNome());

            // 🔹 Retorna a resposta completa
            return ResponseEntity.ok().body(
                String.format("{\"token\":\"%s\",\"type\":\"Bearer\",\"user\":\"%s\"}", token, usuario.getNome())
            );

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }
}
