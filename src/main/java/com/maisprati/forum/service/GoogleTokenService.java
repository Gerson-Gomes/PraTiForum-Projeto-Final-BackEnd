package com.maisprati.forum.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class GoogleTokenService {

    public Map<String, String> getTokens(String authorizationCode) {
        // URL para trocar o código de autorização por tokens
        String tokenEndpoint = "https://oauth2.googleapis.com/token";

        // Definindo os parâmetros para a solicitação
        Map<String, String> params = new HashMap<>();
        params.put("code", authorizationCode);
        params.put("client_id", "YOUR_GOOGLE_CLIENT_ID");
        params.put("client_secret", "YOUR_GOOGLE_CLIENT_SECRET");
        params.put("redirect_uri", "YOUR_REDIRECT_URI");
        params.put("grant_type", "authorization_code");

        // Usando RestTemplate para enviar a solicitação
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> response = restTemplate.postForObject(tokenEndpoint, params, Map.class);

        return response;
    }
}
