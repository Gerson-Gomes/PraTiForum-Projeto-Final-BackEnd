package com.maisprati.forum.dto;

import lombok.Data;

@Data
public class SocialMediaDto {
    private String gitProfile;
    private String discordProfile;
    private String linkedinProfile;
    private String instagramProfile;

    // Construtor com parametros para os perfis
    public SocialMediaDto(String gitProfile, String discordProfile, String linkedinProfile, String instagramProfile) {
        this.gitProfile = gitProfile;
        this.discordProfile = discordProfile;
        this.linkedinProfile = linkedinProfile;
        this.instagramProfile = instagramProfile;
    }
}
