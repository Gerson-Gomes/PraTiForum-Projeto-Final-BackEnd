package com.maisprati.forum.controller;

import com.maisprati.forum.dto.request.LoginRegisterDto;
import com.maisprati.forum.dto.request.UserRegisterDto;
import com.maisprati.forum.dto.response.LoginResponseDto;
import com.maisprati.forum.dto.response.UserRegisterResponseDto;
import com.maisprati.forum.exception.UserNotFoundException;
import com.maisprati.forum.model.User;
import com.maisprati.forum.service.UserService;
import com.maisprati.forum.utils.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null) {
            return ResponseEntity.badRequest().body("Refresh token is missing");
        }

        try {
            String email = tokenService.extractUsername(refreshToken);
            User user = userService.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
            String newAccessToken = tokenService.generateToken(user.getEmail(), user.getId());
            String newRefreshToken = tokenService.generateRefreshToken(user.getEmail());
            userService.storeRefreshToken(user.getEmail(), newRefreshToken);
            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", newAccessToken);
            tokens.put("refreshToken", newRefreshToken);
            return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }

}
