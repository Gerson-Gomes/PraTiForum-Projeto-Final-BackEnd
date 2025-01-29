package com.maisprati.forum.dto.response;

public class FavoriteTopicResponseDto {
    private Long topicId;
    private Long userId;

    public FavoriteTopicResponseDto(Long topicId, Long userId) {
        this.topicId = topicId;
        this.userId = userId;
    }

    public Long getTopicId() {
        return topicId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
