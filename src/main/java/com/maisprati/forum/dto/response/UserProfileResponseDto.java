package com.maisprati.forum.dto.response;

import com.maisprati.forum.dto.SocialMediaDto;
import com.maisprati.forum.model.User;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserProfileResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String description;
    private LocalDate birthDate;
    private LocalDateTime creationDate;
    private LocalDateTime lastEditionDate;
    private List<SocialMediaDto> socialMedia;
    private byte[] profilePicture;

    public UserProfileResponseDto(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.description = user.getDescription();
        this.birthDate = user.getBirthDate();
        this.creationDate = user.getCreationDate();
        this.lastEditionDate = user.getLastEditionDate();
        this.profilePicture = user.getProfilePicture();

        if (user.getUserSocialMidia() != null) {
            this.socialMedia = user.getUserSocialMidia().stream()
                    .map(sm -> {
                        SocialMediaDto dto = new SocialMediaDto();
                        dto.setGitProfile(sm.getGitProfile());
                        dto.setDiscordProfile(sm.getDiscordProfile());
                        dto.setLinkedinProfile(sm.getLinkedinProfile());
                        dto.setInstagramProfile(sm.getInstagramProfile());
                        return dto;
                    })
                    .collect(Collectors.toList());
        }
    }
}