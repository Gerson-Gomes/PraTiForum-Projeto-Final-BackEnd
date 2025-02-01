package com.maisprati.forum.dto.request;

import lombok.Data;

@Data
public class TopicRegisterDto {
    private String title;
    private String content;
    private Long tadId;
}
