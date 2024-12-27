package com.maisprati.forum.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "topics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 100, nullable = false, unique = true)
    @Size(max = 100)
    @NotNull
    private String title;

    @Column(name = "content", length = 600, nullable = false)
    @Size(max = 600)
    @NotNull
    private String content;

    @Column(name = "creation_date",updatable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    @Column(name = "last_edition_date")
    private LocalDateTime lastEditionDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    @ManyToMany
    @JoinTable(
            name = "topic_tag",
            joinColumns = @JoinColumn(name = "topic_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    @ManyToMany
    @JoinTable(
            name = "users_favorites",
            joinColumns = @JoinColumn(name = "topics_id"),
            inverseJoinColumns = @JoinColumn(name = "users_id")
    )
    private List<User> usersWhoFavorited;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Response> responses;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;
}


