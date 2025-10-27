package com.serratec.ecommerce.securitys;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  JwtUtil jwtUtil,
                                  UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain chain)
            throws IOException, ServletException {

        String path = request.getServletPath();
        String method = request.getMethod();

        // 🚫 Ignorar rotas públicas
        if (path.startsWith("/auth/") ||
            path.startsWith("/public/") ||
            (path.startsWith("/categorias") && method.equalsIgnoreCase("GET")) ||
            (path.startsWith("/produtos") && method.equalsIgnoreCase("GET")) ||
            (path.startsWith("/pedidos") && (
                method.equalsIgnoreCase("GET") ||
                method.equalsIgnoreCase("POST") ||
                method.equalsIgnoreCase("PUT")
            )) ||
            path.startsWith("/swagger-ui") ||
            path.startsWith("/v3/api-docs") ||
            path.startsWith("/swagger-resources") ||
            path.startsWith("/webjars")) {
            
            chain.doFilter(request, response);
            return;
        }

        // 🔒 Validação do token JWT
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                String username = jwtUtil.extractUsername(token);
                System.out.println("🔑 Token recebido para usuário: " + username);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails user = userDetailsService.loadUserByUsername(username);

                    if (jwtUtil.isTokenValid(token, username)) {
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        System.out.println("✅ Autenticação via JWT concluída para: " + username);
                    } else {
                        System.out.println("⚠️ Token inválido para usuário: " + username);
                    }
                }

            } catch (Exception e) {
                System.out.println("❌ Erro ao validar token: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Token inválido ou expirado\"}");
                return;
            }
        } else {
            System.out.println("ℹ️ Nenhum header Authorization encontrado");
        }

        chain.doFilter(request, response);
    }
}
