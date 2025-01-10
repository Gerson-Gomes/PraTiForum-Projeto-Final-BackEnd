package com.maisprati.forum.service;

import com.maisprati.forum.dto.UserDto;
import com.maisprati.forum.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private JwtUtil jwtUtil;

    public String generateTokenForUser(UserDto userDto) {
        return jwtUtil.generateToken(userDto.getEmail());
    }
}
