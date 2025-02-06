package com.maisprati.forum.dto.request;

import com.maisprati.forum.model.Tag;
import com.maisprati.forum.model.Topic;
import lombok.Data;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TopicDto {
    private Long id;
    private String title;
    private String content;
    private List<Long> tagIds; // Adicionado para vincular as tags

    public TopicDto (Topic topic){
        this.id = topic.getId();
        this.title = topic.getTitle();
        this.content = topic.getContent();
        this.tagIds = topic.getTags().stream().map(Tag::getId).collect(Collectors.toList());
    }
}
