package com.maisprati.forum.dto.response;

import com.maisprati.forum.dto.SocialMediaDto;
import com.maisprati.forum.model.User;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserProfileResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String description;
    private String birthDate;
    private String creationDate;
    private String lastEditionDate;
    private SocialMediaDto socialMedia;
    private byte[] profilePicture;

    public UserProfileResponseDto(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.description = user.getDescription();
        this.birthDate = formatBirthDate(user.getBirthDate());
        this.creationDate = formatDate(user.getCreationDate());
        this.lastEditionDate = formatDate(user.getLastEditionDate());
        this.profilePicture = user.getProfilePicture();
        this.socialMedia = new SocialMediaDto(
                user.getUserSocialMidia().getGitProfile(),
                user.getUserSocialMidia().getDiscordProfile(),
                user.getUserSocialMidia().getLinkedinProfile(),
                user.getUserSocialMidia().getInstagramProfile());
    }

    private String formatBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return birthDate.format(formatter);
    }

    private String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return dateTime.format(formatter);
    }
}