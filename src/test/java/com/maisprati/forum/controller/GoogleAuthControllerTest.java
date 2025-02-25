package com.maisprati.forum.controller;

import com.maisprati.forum.dto.response.LoginResponseDto;
import com.maisprati.forum.model.User;
import com.maisprati.forum.service.GoogleTokenService;
import com.maisprati.forum.service.GoogleTokenVerifier;
import com.maisprati.forum.service.UserService;
import com.maisprati.forum.utils.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class GoogleAuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GoogleTokenService googleTokenService;

    @Mock
    private GoogleTokenVerifier googleTokenVerifier;

    @Mock
    private UserService userService;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private GoogleAuthController googleAuthController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(googleAuthController).build();
    }

    @Test
    public void testAuthenticateWithGoogle() throws Exception {
        mockMvc.perform(get("/auth/google"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testExchangeAuthorizationCode() throws Exception {
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", "google-access-token");
        tokens.put("refresh_token", "google-refresh-token");

        User googleUser = new User();
        googleUser.setEmail("user@gmail.com");

        User registeredUser = new User();
        registeredUser.setId(1L);
        registeredUser.setEmail("user@gmail.com");

        given(googleTokenService.getTokens(anyString())).willReturn(tokens);
        given(googleTokenVerifier.verifyToken(anyString())).willReturn(googleUser);
        given(userService.registerUserGoogle(googleUser)).willReturn(registeredUser);
        given(tokenService.generateToken(anyString(), any(Long.class))).willReturn("jwt-token");

        mockMvc.perform(get("/login/oauth2/code/google")
                        .param("code", "authorization-code"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.userRole").value(registeredUser.getRole().toString()));
    }
}
