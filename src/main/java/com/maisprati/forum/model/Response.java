package com.maisprati.forum.model;

import com.fasterxml.jackson.annotation.JsonIgnore; // Importando a anotação
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "responses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", length = 600, nullable = false)
    @Size(max = 600)
    @NotNull
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    @JsonIgnore // Ignorando a serialização de 'user'
    private User user;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    @NotNull
    @JsonIgnore // Ignorando a serialização de 'topic'
    private Topic topic;

    @Column(name = "creation_date", updatable = false)
    @JsonIgnore
    private LocalDateTime creationDate = LocalDateTime.now();


    @Column(name = "last_edition_date")
    @JsonIgnore
    private LocalDateTime lastEditionDate;

}
