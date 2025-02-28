package com.maisprati.forum.controller;

import com.maisprati.forum.dto.request.LoginRegisterDto;
import com.maisprati.forum.dto.request.UserRegisterDto;
import com.maisprati.forum.dto.response.LoginResponseDto;
import com.maisprati.forum.dto.response.UserRegisterResponseDto;
import com.maisprati.forum.model.User;
import com.maisprati.forum.service.UserService;
import com.maisprati.forum.utils.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testRegisterUser() throws Exception {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setFullName("John Doe");
        userRegisterDto.setEmail("john.doe@example.com");
        userRegisterDto.setPassword("password123");
        userRegisterDto.setConfirmPassword("password123");

        given(userService.registerUser(any(UserRegisterDto.class))).willReturn(new UserRegisterResponseDto(new User()));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\": \"John Doe\", \"email\": \"john.doe@example.com\", \"password\": \"password123\", \"confirmPassword\": \"password123\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testLoginUser() throws Exception {
        LoginRegisterDto loginRegisterDto = new LoginRegisterDto();
        loginRegisterDto.setEmail("john.doe@example.com");
        loginRegisterDto.setPassword("password123");

        User user = new User();
        user.setId(1L);
        user.setEmail("john.doe@example.com");

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, "password", user.getAuthorities());

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(authentication);

        given(tokenService.generateToken(anyString(), any(Long.class))).willReturn("jwt-token");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"john.doe@example.com\", \"password\": \"password123\"}"))
                .andExpect(status().isOk());
    }
}
