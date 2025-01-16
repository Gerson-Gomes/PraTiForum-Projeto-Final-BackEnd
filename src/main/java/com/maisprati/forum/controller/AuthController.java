package com.maisprati.forum.controller;

import com.maisprati.forum.dto.LoginDto;
import com.maisprati.forum.dto.LoginResponseDto;
import com.maisprati.forum.dto.UserRegisterDto;
import com.maisprati.forum.model.User;
import com.maisprati.forum.service.TokenService;
import com.maisprati.forum.service.UserService;
import com.maisprati.forum.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterDto userDto) {
        var userResponse =  userService.registerUser(userDto);
        return ResponseEntity.ok().body(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDto loginDto) {
        var userAuth = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        var auth = this.authenticationManager.authenticate(userAuth);

        User user = (User) auth.getPrincipal();
        var token = jwtUtil.generateToken(user);

        return ResponseEntity.ok().body(new LoginResponseDto(token));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody String email) {
        return userService.forgotPassword(email);
    }
}
