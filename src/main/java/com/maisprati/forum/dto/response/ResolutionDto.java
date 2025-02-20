package com.maisprati.forum.dto.response;

import com.maisprati.forum.model.Resolution;

public class ResolutionDto {
    private Long id;
    private Long responseId;
    private Long topicId;

    public ResolutionDto(Resolution resolution) {
        this.id = resolution.getId();
        this.responseId = resolution.getResponse().getId();
        this.topicId = resolution.getTopic().getId();
    }

    public Long getId() {
        return id;
    }

    public Long getResponseId() {
        return responseId;
    }

    public Long getTopicId() {
        return topicId;
    }
}
