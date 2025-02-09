package com.maisprati.forum.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteTopicResponseDto {
    private Long topicId;
    private Long userId;
}
