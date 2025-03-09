package com.maisprati.forum.dto.response;

import com.maisprati.forum.model.Tag;
import com.maisprati.forum.model.Topic;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class TopicResponseDto {
    private Long id;
    private String title;
    private String content;
    private int likes;
    private List<Long> tagIds;

    public TopicResponseDto(Topic topic){
        this.id = topic.getId();
        this.title = topic.getTitle();
        this.content = topic.getContent();
        this.likes = topic.getLikes() != null ? topic.getLikes().size() : 0;
        this.tagIds = topic.getTags().stream().map(Tag::getId).collect(Collectors.toList());
    }

    public Topic toTopic() {
        Topic topic = new Topic();
        topic.setId(this.getId());
        topic.setTitle(this.getTitle());
        topic.setContent(this.getContent());
        return topic;
    }
}
