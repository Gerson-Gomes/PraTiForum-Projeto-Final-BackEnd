package com.maisprati.forum.dto.response;

import com.maisprati.forum.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDto {
    private String token;
    private UserRole userRole;
}
