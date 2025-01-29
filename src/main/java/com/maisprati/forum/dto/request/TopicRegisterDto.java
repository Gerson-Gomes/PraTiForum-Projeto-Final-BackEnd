package com.maisprati.forum.dto.request;

public class TopicRegisterDto {
    private String title;
    private String content;
    private Long tadId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTadId() {
        return tadId;
    }

    public void setTadId(Long tadId) {
        this.tadId = tadId;
    }
}
