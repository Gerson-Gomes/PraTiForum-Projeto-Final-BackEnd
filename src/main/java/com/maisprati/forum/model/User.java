package com.maisprati.forum.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.management.relation.Role;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", length = 25, nullable = true, unique = true)
    @Size(max = 25)
    private String userName;

    @Column(name = "first_name", length = 100, nullable = false)
    @Size(max = 100)
    @NotNull
    private String firstName;

    @Column(name = "last_name", length = 100, nullable = true)
    @Size(max = 100)
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

    @Column(name = "birth_date", nullable = true)
    private LocalDate birthDate;

    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    @Column(name = "last_edition_date")
    private LocalDateTime lastEditionDate;

    @Column(name = "profile_picture")
    private byte[] profilePicture;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

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


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN){
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER"));
        } else return  List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}



