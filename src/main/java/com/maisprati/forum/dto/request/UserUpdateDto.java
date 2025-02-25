package com.maisprati.forum.dto.request;

import com.maisprati.forum.dto.SocialMediaDto;
import lombok.Data;
import jakarta.validation.constraints.*;
import java.util.List;

@Data
public class UserUpdateDto {
    @NotBlank(message = "O primeiro nome é obrigatório")
    private String firstName;

    @NotBlank(message = "O sobrenome é obrigatório")
    private String lastName;

    @Email(message = "E-mail inválido")
    private String email;

    private String description;

    private String birthDate;

    private List<SocialMediaDto> socialMedia;
}
