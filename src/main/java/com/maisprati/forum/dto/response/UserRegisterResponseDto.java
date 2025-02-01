package com.maisprati.forum.dto.response;

import com.maisprati.forum.model.User;
import lombok.Data;

@Data
public class UserRegisterResponseDto {
    private String fullName;
    private String email;

    public UserRegisterResponseDto(User user) {
        this.fullName = user.getFirstName();
        this.email = user.getEmail();
    }

}