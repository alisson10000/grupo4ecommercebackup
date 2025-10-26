package com.serratec.ecommerce.securitys;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "uma_chave_muito_grande_e_segura_1234567890"; // mínimo 32 caracteres

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    //  Gera o token JWT
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //  Extrai todas as claims (informações do token)
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //  Verifica se o token ainda é válido
    public boolean isTokenValid(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return username.equals(tokenUsername) && !isTokenExpired(token);
    }

    //  Verifica se o token expirou
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    //  Extrai o username do token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }
}
