package com.serratec.ecommerce.securitys;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serratec.ecommerce.dtos.LoginDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.io.IOException;
import com.serratec.ecommerce.entitys.Usuario;
import com.serratec.ecommerce.repositorys.UsuarioRepository;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;


    // 🔹 Construtor com dois parâmetros (igual ao que o Spring espera)
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
        setFilterProcessesUrl("/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            LoginDTO login = mapper.readValue(request.getInputStream(), LoginDTO.class);

            System.out.println("Tentando autenticar usuário: " + login.getEmail());

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword());

            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler credenciais: " + e.getMessage(), e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        String username = ((UserDetails) authResult.getPrincipal()).getUsername();
        String token = jwtUtil.generateToken(username);

        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("access-control-expose-headers", "Authorization");
        response.setContentType("application/json");

        String jsonResponse = String.format("{\"token\":\"%s\",\"type\":\"Bearer\",\"user\":\"%s\"}", token, username);
        response.getWriter().write(jsonResponse);

        System.out.println("✅ Login bem-sucedido! Token JWT gerado para: " + username);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Usuário ou senha inválidos\"}");

        System.out.println("❌ Falha na autenticação: " + failed.getMessage());
    }
}
