package com.maisprati.forum.dto.request;

import com.maisprati.forum.dto.SocialMediaDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserUpdateDto {
    private String firstName;
    private String lastName;
    private String email;
    private String description;
    private String birthDate;
    private List<SocialMediaDto> socialMedia;
}
