
package com.maisprati.forum.model;
;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "users_social_midia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSocialMidia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "git_profile")
    private String gitProfile;

    @Column(name = "discord_profile")
    private String discordProfile;

    @Column(name = "linkedin_profile")
    private String linkedinProfile;

    @Column(name = "instagram_profile")
    private String instagramProfile;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public UserSocialMidia(String gitProfile, String discordProfile, String linkedinProfile, String instagramProfile) {
        this.gitProfile = gitProfile;
        this.discordProfile = discordProfile;
        this.linkedinProfile = linkedinProfile;
        this.instagramProfile = instagramProfile;
    }
}
