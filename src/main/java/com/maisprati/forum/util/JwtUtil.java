package com.maisprati.forum.util;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.maisprati.forum.model.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;


@Service
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Extrai o username (subject) de um token JWT.
     */
    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao extrair o nome de usuário do token", e);
        }
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
    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Token inválido ou expirado", e);
        }
    }

    /**
     * Valida um token JWT contra as credenciais do usuário.
     */
    public String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return com.auth0.jwt.JWT.create()
                .withSubject(user.getUsername()) // Define o "subject" (usuário)
                .withIssuedAt(new Date()) // Define a data de emissão
                .withExpiresAt(generateExpirationDate()) // Define a data de expiração
                .sign(algorithm); // Assina o token com o algoritmo e a chave secreta
    }

    public String validateToken(String token) {
        try {
            // Verifica a assinatura e decodifica o token
            DecodedJWT decodedJWT = com.auth0.jwt.JWT.require(
                    Algorithm.HMAC256(secret)) // Algoritmo com chave secreta
                    .build()
                    .verify(token); // Verifica o token

            // Verifica se o token não está expirado
            Date expirationDate = decodedJWT.getExpiresAt();
            if (expirationDate.before(new Date())) {
                System.out.println("Token expirado.");
                return null;
            }

            return decodedJWT.getSubject(); // Token válido

        } catch (ExpiredJwtException e) {
            System.out.println("Token expirado.");
        } catch (SignatureVerificationException e) {
            System.out.println("Assinatura do token inválida.");
        } catch (Exception e) {
            System.out.println("Erro ao validar o token: " + e.getMessage());
        }
        return null;
    }


    /**
     * Verifica se o token está expirado.
     */
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrai a data de expiração do token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Método para gerar a data de expiração (20 minutos)
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + calculateExpirationInMillis());
    }

    // Método para calcular o tempo de expiração em milissegundos (20 minutos)
    private long calculateExpirationInMillis() {
        return 1000 * 60 * 20; // 20 minutos em milissegundos
    }
}
