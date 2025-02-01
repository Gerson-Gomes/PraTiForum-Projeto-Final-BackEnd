
package com.maisprati.forum.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "connections")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    @NotNull
    private User follower;

    @ManyToOne
    @JoinColumn(name = "followed_id", nullable = false)
    @NotNull
    private User followed;

    @Column(name = "followed_date", nullable = false, updatable = false)
    private LocalDateTime followedAt = LocalDateTime.now();
}
