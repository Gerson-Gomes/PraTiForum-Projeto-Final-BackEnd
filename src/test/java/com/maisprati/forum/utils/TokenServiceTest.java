package com.maisprati.forum.utils;

import com.maisprati.forum.exception.TokenInvalidExpection;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TokenServiceTest {

    private TokenService tokenService;
    private Key signingKey;

    @BeforeEach
    public void setUp() {
        tokenService = new TokenService();
        signingKey = Keys.hmacShaKeyFor("secret-key".getBytes(StandardCharsets.UTF_8));
        ReflectionTestUtils.setField(tokenService, "jwtSecret", "secret-key");
        ReflectionTestUtils.setField(tokenService, "jwtExpirationInMs", 3600000);
        ReflectionTestUtils.setField(tokenService, "jwtRefreshExpirationInMs", 86400000); // 24 horas
    }

    @Test
    public void testGenerateToken() {
        String token = tokenService.generateToken("user@example.com", 1L);

        assertNotNull(token);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("user@example.com", claims.getSubject());
        assertEquals(1L, claims.get("userId"));
    }

    @Test
    public void testExtractUsername() {
        String token = tokenService.generateToken("user@example.com", 1L);

        String username = tokenService.extractUsername(token);

        assertEquals("user@example.com", username);
    }

    @Test
    public void testExtractAllClaims() {
        String token = tokenService.generateToken("user@example.com", 1L);

        Claims claims = tokenService.extractAllClaimsForTest(token);

        assertNotNull(claims);
        assertEquals("user@example.com", claims.getSubject());
    }

    @Test
    public void testValidateToken() {
        String token = tokenService.generateToken("user@example.com", 1L);

        boolean isValid = tokenService.validateToken(token, "user@example.com");

        assertTrue(isValid);
    }

    @Test
    public void testValidateTokenThrowsExceptionWhenInvalid() {
        String token = Jwts.builder()
                .setSubject("user@example.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() - 1000)) // Token expirado
                .signWith(SignatureAlgorithm.HS256, "invalid-secret-key")
                .compact();

        assertThrows(TokenInvalidExpection.class, () -> tokenService.extractAllClaimsForTest(token));
    }
}
