package com.maisprati.forum.dto.response;

import com.maisprati.forum.model.Connection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConnectionResponseDto {
    private Long id;
    private Long followerId;
    private Long followedId;
    private LocalDateTime followedAt;

    public ConnectionResponseDto(Connection connection) {
        this.id = connection.getId();
        this.followerId = connection.getFollower().getId();
        this.followedId = connection.getFollowed().getId();
        this.followedAt = connection.getFollowedAt();
    }
}