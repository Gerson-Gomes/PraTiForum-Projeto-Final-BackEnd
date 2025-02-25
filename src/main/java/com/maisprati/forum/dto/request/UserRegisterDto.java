package com.maisprati.forum.dto.request;

import com.maisprati.forum.model.User;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.validation.constraints.*;

@Data
public class UserRegisterDto {
    @NotBlank(message = "O nome completo é obrigatório")
    private String fullName;

    @Email(message = "E-mail inválido")
    @NotBlank(message = "O e-mail é obrigatório")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
    private String password;

    @NotBlank(message = "A confirmação de senha é obrigatória")
    private String confirmPassword;

    public User createUser(UserRegisterDto userDto, PasswordEncoder passwordEncoder){
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setUserName(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setFirstName(userDto.getFullName());
        return user;
    }
}