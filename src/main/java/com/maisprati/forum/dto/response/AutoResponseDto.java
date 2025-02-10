package com.maisprati.forum.dto.response;

import com.maisprati.forum.model.Auto;
import lombok.Data;

@Data
public class AutoResponseDto {
    private Long id;
    private String name;
    private String model;
    private int year;

    public AutoResponseDto(Auto auto) {
        this.id = auto.getId();
        this.name = auto.getName();
        this.model = auto.getModel();
        this.year = auto.getYear();
    }
}
