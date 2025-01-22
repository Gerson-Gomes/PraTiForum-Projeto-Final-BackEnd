package com.maisprati.forum.dto;


import lombok.Data;

@Data
public class UserRegisterDto {
    private String fullName;
    private String email;
    private String password;
    private String confirmPassword;


}
