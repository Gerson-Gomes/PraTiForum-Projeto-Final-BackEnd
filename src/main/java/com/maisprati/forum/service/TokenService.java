package com.maisprati.forum.service;

import com.maisprati.forum.dto.UserRegisterDto;
import com.maisprati.forum.model.User;
import com.maisprati.forum.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Autowired
    private JwtUtil jwtUtil;

    public String generateTokenForUser(User user) {
        return jwtUtil.generateToken(user.getEmail());
    }
}
