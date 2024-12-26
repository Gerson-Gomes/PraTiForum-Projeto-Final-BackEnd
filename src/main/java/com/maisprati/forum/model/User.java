package com.maisprati.forum.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", length = 25, nullable = false, unique = true)
    private String userName;

    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    @Column(name = "email", length = 320, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    @Column(name = "last_edition_date")
    private LocalDateTime lastEditionDate;

    //VERIFICAR SITUACAO DE ARMAZENAMENTO DE FOTO DE PERFIL USUÁRIO
    @Column(name = "profile_picture")
    private byte[] profilePicture;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Topic> topics;

    @OneToMany(mappedBy = "user")
    private List<UserSocialMidia> userSocialMidia;

    @ManyToMany(mappedBy = "usersWhoFavorited")
    private List<Topic> FavoritedTopics;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Response> respons;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
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

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }



    public List<Topic> getFavoritedTopics() {
        return FavoritedTopics;
    }

    public void setFavoritedTopics(List<Topic> favoritedTopics) {
        FavoritedTopics = favoritedTopics;
    }

    public List<Response> getResponses() {
        return respons;
    }

    public void setResponses(List<Response> respons) {
        this.respons = respons;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }


    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public List<UserSocialMidia> getUserSocialMidia() {
        return userSocialMidia;
    }

    public void setUserSocialMidia(List<UserSocialMidia> userSocialMidia) {
        this.userSocialMidia = userSocialMidia;
    }

    public List<Response> getRespons() {
        return respons;
    }

    public void setRespons(List<Response> respons) {
        this.respons = respons;
    }
}
