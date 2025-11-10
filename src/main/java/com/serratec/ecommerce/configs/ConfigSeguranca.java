package com.serratec.ecommerce.configs;

import com.serratec.ecommerce.repositorys.UsuarioRepository;
import com.serratec.ecommerce.securitys.JwtAuthenticationFilter;
import com.serratec.ecommerce.securitys.JwtAuthorizationFilter;
import com.serratec.ecommerce.securitys.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class ConfigSeguranca {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UsuarioRepository usuarioRepository;

    public ConfigSeguranca(
            JwtUtil jwtUtil,
            UserDetailsService userDetailsService,
            AuthenticationConfiguration authenticationConfiguration,
            UsuarioRepository usuarioRepository
    ) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationConfiguration = authenticationConfiguration;
        this.usuarioRepository = usuarioRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = authenticationManager(authenticationConfiguration);

        JwtAuthenticationFilter authenticationFilter =
                new JwtAuthenticationFilter(authenticationManager, jwtUtil, usuarioRepository);
        authenticationFilter.setFilterProcessesUrl("/auth/login");

        JwtAuthorizationFilter authorizationFilter =
                new JwtAuthorizationFilter(authenticationManager, jwtUtil, userDetailsService);

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // 🔓 ROTAS PÚBLICAS
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/imagens/**").permitAll() // ✅ libera acesso às imagens
                .requestMatchers("/uploads/**").permitAll() // ✅ libera também caso use /uploads/
                .requestMatchers(HttpMethod.GET, "/public/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/categorias/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/produtos/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/pedidos/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/pedidos/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/pedidos/**").permitAll()

                // 🔓 ROTAS SWAGGER
                .requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**",
                    "/v3/api-docs",
                    "/swagger-resources/**",
                    "/webjars/**"
                ).permitAll()

                // 🔒 ROTAS ADMIN
                .requestMatchers(HttpMethod.GET, "/usuarios/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/usuarios/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/categorias/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/categorias/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/categorias/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/produtos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/produtos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/produtos/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.GET, "/clientes/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/clientes/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/clientes/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/clientes/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.GET, "/relatorios/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/relatorios/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/relatorios/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/relatorios/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.DELETE, "/pedidos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/validar-cupom/**").hasRole("ADMIN")

                // 🔒 Qualquer outra rota precisa estar autenticada
                .anyRequest().authenticated()
            )
            // Filtros JWT
            .addFilter(authenticationFilter)
            .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
