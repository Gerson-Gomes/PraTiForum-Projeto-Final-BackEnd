package com.maisprati.forum.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "responses")
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", length = 600, nullable = false)
    private String content;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;


    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    @Column(name = "last_edition_date")
    private LocalDateTime lastEditionDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastEditionDate() {
        return lastEditionDate;
    }

    public void setLastEditionDate(LocalDateTime lastEditionDate) {
        this.lastEditionDate = lastEditionDate;
    }

}
