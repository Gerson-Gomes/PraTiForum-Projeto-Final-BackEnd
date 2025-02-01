package com.maisprati.forum.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FavoriteTopicResponseDto {
    private Long topicId;
    private Long userId;
}
