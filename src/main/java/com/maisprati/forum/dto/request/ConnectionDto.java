package com.maisprati.forum.dto.request;

public class ConnectionDto {
    private Long followerId;  // ID do usuário seguidor
    private Long followedId;  // ID do usuário seguido

    // Getter para o ID do seguidor
    public Long getFollowerId() {
        return followerId;
    }

    // Setter para o ID do seguidor
    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }

    // Getter para o ID do seguido
    public Long getFollowedId() {
        return followedId;
    }

    // Setter para o ID do seguido
    public void setFollowedId(Long followedId) {
        this.followedId = followedId;
    }
}
