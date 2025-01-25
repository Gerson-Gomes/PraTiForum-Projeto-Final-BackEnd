package com.maisprati.forum.controller;

import com.maisprati.forum.dto.request.LoginRegisterDto;
import com.maisprati.forum.dto.request.TokenRequest;
import com.maisprati.forum.dto.response.LoginResponseDto;
import com.maisprati.forum.dto.request.UserRegisterDto;
import com.maisprati.forum.model.GoogleUser;
import com.maisprati.forum.model.User;
import com.maisprati.forum.model.UserRole;
import com.maisprati.forum.service.GoogleTokenVerifier;
import com.maisprati.forum.service.UserService;
import com.maisprati.forum.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private GoogleTokenVerifier googleTokenVerifier;

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
        var token = tokenService.generateToken(user.getUsername());

        return ResponseEntity.ok().body(new LoginResponseDto(token, user.getRole()));
    }

    @PostMapping("/google")
    public ResponseEntity<?> authenticateGoogleUser(@RequestBody TokenRequest tokenRequest) {
        String googleToken = tokenRequest.getToken();
        GoogleUser googleUser = googleTokenVerifier.verifyToken(googleToken);

        if (googleUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }

        String jwt = tokenService.generateToken(googleUser.getEmail());
        return ResponseEntity.ok(new LoginResponseDto(jwt, UserRole.USER));
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess() {
        return "Login bem-sucedido!";
    }

    @GetMapping("/loginFailure")
    public String loginFailure() {
        return "Falha no login.";
    }
}
