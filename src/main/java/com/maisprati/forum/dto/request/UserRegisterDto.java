package com.maisprati.forum.dto.request;


import lombok.Data;

@Data
public class UserRegisterDto {
    private String fullName;
    private String email;
    private String password;
    private String confirmPassword;


}
