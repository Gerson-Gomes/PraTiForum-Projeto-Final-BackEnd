package com.maisprati.forum.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class AutoRequestDto {
    @NotBlank(message = "O nome não pode estar em branco")
    private String name;

    @NotBlank(message = "O modelo não pode estar em branco")
    private String model;

    @Min(value = 1886, message = "O ano deve ser válido")
    private int year;
}