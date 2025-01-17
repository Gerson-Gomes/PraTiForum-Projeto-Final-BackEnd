package com.maisprati.forum.service.token;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;


@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Gera uma chave de assinatura com o segredo
     * @return
     */

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Extrai o username (subject) de um token JWT.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrai um claim genérico do token usando uma função fornecida.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrai todos os claims de um token JWT.
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token expirado.", e);
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Token inválido ou mal formado.", e);
        }
    }

    /**
     * Gera um token JWT para um usuário.
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Define o "subject" (nome do usuário)
                .setIssuedAt(new Date()) // Data de emissão
                .setExpiration(generateExpirationDate())
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Assina o token
                .compact();
    }

    /**
     * Valida o token JWT.
     */
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    /**
     * Verifica se o token está expirado.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrai a data de expiração do token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     Método para gerar a data de expiração (20 minutos)
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + calculateExpirationInMillis());
    }

    /**
    Metodo para calcular data de expiração
     */
    private long calculateExpirationInMillis() {
        return 1000 * 60 * 20; // 20 minutos em milissegundos
    }

}
