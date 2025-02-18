package com.maisprati.forum.dto.response;

import com.maisprati.forum.model.Response;

public class ResponseDto {
    private Long id;
    private String content;

    public ResponseDto(Response response) {
        this.id = response.getId();
        this.content = response.getContent();
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
