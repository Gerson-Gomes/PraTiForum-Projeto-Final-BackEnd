package com.maisprati.forum.controller;

import com.maisprati.forum.dto.response.LoginResponseDto;
import com.maisprati.forum.model.User;
import com.maisprati.forum.model.UserRole;
import com.maisprati.forum.service.GoogleTokenService;
import com.maisprati.forum.service.GoogleTokenVerifier;
import com.maisprati.forum.service.UserService;
import com.maisprati.forum.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
public class GoogleAuthController {

    @Autowired
    private GoogleTokenService googleTokenService;

    @Autowired
    private GoogleTokenVerifier googleTokenVerifier;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @GetMapping("/auth/google")
    public void authenticateWithGoogle(HttpServletResponse response) throws IOException {
        String authorizationEndpoint = "https://accounts.google.com/o/oauth2/auth";
        String scope = "openid email profile";
        String responseType = "code";

        String url = String.format("%s?client_id=%s&redirect_uri=%s&scope=%s&response_type=%s",
                authorizationEndpoint, clientId, redirectUri, scope, responseType);

        response.sendRedirect(url);
    }

    @GetMapping("/login/oauth2/code/google")
    public ResponseEntity<?> exchangeAuthorizationCode(@RequestParam("code") String code) {
        Map<String, String> tokens = googleTokenService.getTokens(code);

        String accessToken = tokens.get("access_token");
        String refreshToken = tokens.get("refresh_token");

        User googleUser = googleTokenVerifier.verifyToken(accessToken);

        userService.storeRefreshToken(googleUser.getEmail(), refreshToken);

        // Gerando o JWT incluindo o email e o ID do usuário
        String jwt = tokenService.generateToken(googleUser.getEmail(), googleUser.getId());
        return ResponseEntity.ok(new LoginResponseDto(jwt, UserRole.USER));
    }
}
