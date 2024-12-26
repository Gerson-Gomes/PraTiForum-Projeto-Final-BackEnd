package com.maisprati.forum.model;

import jakarta.persistence.*;


@Entity
@Table(name = "users_social_midia")
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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGitProfile() {
        return gitProfile;
    }

    public void setGitProfile(String gitProfile) {
        this.gitProfile = gitProfile;
    }

    public String getDiscordProfile() {
        return discordProfile;
    }

    public void setDiscordProfile(String discordProfile) {
        this.discordProfile = discordProfile;
    }

    public String getLinkedinProfile() {
        return linkedinProfile;
    }

    public void setLinkedinProfile(String linkedinProfile) {
        this.linkedinProfile = linkedinProfile;
    }

    public String getInstagramProfile() {
        return instagramProfile;
    }

    public void setInstagramProfile(String instagramProfile) {
        this.instagramProfile = instagramProfile;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
