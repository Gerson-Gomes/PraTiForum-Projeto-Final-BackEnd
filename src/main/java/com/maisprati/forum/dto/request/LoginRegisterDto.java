package com.maisprati.forum.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class LoginRegisterDto {
    @Email(message = "E-mail inválido")
    @NotBlank(message = "O e-mail é obrigatório")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
    private String password;
}