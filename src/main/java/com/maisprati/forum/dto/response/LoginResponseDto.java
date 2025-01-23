package com.maisprati.forum.dto.response;

import com.maisprati.forum.model.UserRole;
import lombok.Data;


@Data
public class LoginResponseDto {
    private String token;
    private UserRole userRole;

    public LoginResponseDto(String token, UserRole userRole) {
        this.token = token;
        this.userRole = userRole;
    }

}
