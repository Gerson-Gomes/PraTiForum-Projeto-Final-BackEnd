package com.maisprati.forum.dto;

import lombok.Data;
import java.util.List;

@Data
public class TopicDto {
    private Long id;
    private String title;
    private String content;
    private List<Long> tagIds; // Adicionado para vincular as tags
}
