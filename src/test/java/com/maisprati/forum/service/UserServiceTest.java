package com.maisprati.forum.service;

import com.maisprati.forum.dto.request.UserRegisterDto;
import com.maisprati.forum.exception.UserAlreadyExistsException;
import com.maisprati.forum.exception.UserNotFoundException;
import com.maisprati.forum.model.User;
import com.maisprati.forum.repository.UserRepository;
import com.maisprati.forum.utils.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUserNotFoundException() {
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.loadUserByUsername("nonexistentuser"));
    }

    @Test
    void testUserAlreadyExistsException() {
        UserRegisterDto userDto = new UserRegisterDto();
        userDto.setEmail("existinguser@example.com");
        userDto.setPassword("password");
        userDto.setConfirmPassword("password");

        when(userRepository.findByUserName(anyString())).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(userDto));
    }

    @Test
    void testInvalidPasswordException() {
        UserRegisterDto userDto = new UserRegisterDto();
        userDto.setEmail("newuser@example.com");
        userDto.setPassword("password1");
        userDto.setConfirmPassword("password2");

        assertThrows(RuntimeException.class, () -> userService.registerUser(userDto));
    }
}
