package com.maisprati.forum.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", length = 25, nullable = false, unique = true)
    @Size(max = 25)
    @NotNull
    private String userName;

    @Column(name = "first_name", length = 100, nullable = false)
    @Size(max = 100)
    @NotNull
    private String firstName;

    @Column(name = "last_name", length = 100, nullable = false)
    @Size(max = 100)
    @NotNull
    private String lastName;

    @Column(name = "email", length = 320, nullable = false, unique = true)
    @Email
    @NotNull
    private String email;

    @Column(name = "password", length = 100, nullable = false)
    @Size(max = 100)
    @NotNull
    private String password;

    @Column(name = "description", length = 500)
    @Size(max = 500)
    private String description;

    @Column(name = "birth_date", nullable = false)
    @NotNull
    private LocalDate birthDate;

    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    @Column(name = "last_edition_date")
    private LocalDateTime lastEditionDate;

    @Column(name = "profile_picture")
    private byte[] profilePicture;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Topic> topics;

    @OneToMany(mappedBy = "user")
    private List<UserSocialMidia> userSocialMidia;

    @ManyToMany(mappedBy = "usersWhoFavorited")
    private List<Topic> favoritedTopics;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Response> responses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;
}



