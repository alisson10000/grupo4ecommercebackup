package com.serratec.ecommerce.securitys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    // 🔹 Compatível com ambos: só e-mail ou e-mail+nome
    public String generateToken(String username) {
        return generateToken(username, username);
    }

    // 🔹 Gera token JWT com e-mail e nome
    public String generateToken(String username, String nomeUsuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", nomeUsuario);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractName(String token) {
        Object name = extractClaims(token).get("name");
        return name != null ? name.toString() : null;
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public boolean isTokenValid(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return username.equals(tokenUsername) && !isTokenExpired(token);
    }
}
