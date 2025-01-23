package com.maisprati.forum.dto.request;

import lombok.Data;

@Data
public class TopicDto {
    private Long id;
    private String title;
    private String content;
}
