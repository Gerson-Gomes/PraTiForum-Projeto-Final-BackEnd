package com.maisprati.forum.dto.response;

import com.maisprati.forum.model.Topic;
import com.maisprati.forum.model.User;
import lombok.Data;

@Data
public class LikeResponseDto {
    private Long likeId;
    private Long userId;
    private Long topicId;

    public LikeResponseDto(Long likeId,User user, Topic topic) {
        this.likeId = likeId;
        this.userId = user.getId();
        this.topicId = topic.getId();
    }
}
