package com.maisprati.forum.dto.request;


import com.maisprati.forum.model.User;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class UserRegisterDto {
    private String fullName;
    private String email;
    private String password;
    private String confirmPassword;

    public User createUser(UserRegisterDto userDto, PasswordEncoder passwordEncoder){
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setUserName(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setFirstName(userDto.getFullName());
        return  user;
    }


}
