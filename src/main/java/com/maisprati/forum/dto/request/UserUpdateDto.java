package com.maisprati.forum.dto.request;

import com.maisprati.forum.dto.SocialMediaDto;
import com.maisprati.forum.model.UserSocialMidia;
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
    private String location;
    private SocialMediaDto socialMedia;
    private String profilePicture;

    public UserSocialMidia toUserSocialMidia(){
        return new UserSocialMidia(
                this.socialMedia.getGitProfile(),
                this.socialMedia.getInstagramProfile(),
                this.socialMedia.getLinkedinProfile());

    }
}
