package com.maisprati.forum.controller;

import com.maisprati.forum.dto.request.LoginRegisterDto;
import com.maisprati.forum.dto.response.LoginResponseDto;
import com.maisprati.forum.dto.request.UserRegisterDto;
import com.maisprati.forum.model.User;
import com.maisprati.forum.service.UserService;
import com.maisprati.forum.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterDto userDto) {
        var userResponse = userService.registerUser(userDto);
        return ResponseEntity.ok().body(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRegisterDto loginDto) {
        var userAuth = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        var auth = this.authenticationManager.authenticate(userAuth);

        User user = (User) auth.getPrincipal();
        var token = tokenService.generateToken(user.getUsername(), user.getId());

        return ResponseEntity.ok().body(new LoginResponseDto(token, user.getRole()));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody String email) {
        // Implementar lógica para recuperação de senha, se necessário
        return ResponseEntity.ok("Instruções de recuperação de senha enviadas para: " + email);
    }
}
