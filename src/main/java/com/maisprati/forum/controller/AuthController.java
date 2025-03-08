package com.maisprati.forum.controller;

import com.maisprati.forum.dto.request.LoginRegisterDto;
import com.maisprati.forum.dto.request.UserRegisterDto;
import com.maisprati.forum.dto.response.LoginResponseDto;
import com.maisprati.forum.dto.response.UserRegisterResponseDto;
import com.maisprati.forum.model.User;
import com.maisprati.forum.service.UserService;
import com.maisprati.forum.utils.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterDto userDto) {
        UserRegisterResponseDto userResponse = userService.registerUser(userDto);
        return ResponseEntity.ok().body(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRegisterDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String token = tokenService.generateToken(user.getUsername(), user.getId());

        return ResponseEntity.ok().body(new LoginResponseDto(token, user.getRole()));
    }
}
