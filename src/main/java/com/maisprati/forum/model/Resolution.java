package com.maisprati.forum.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resolutions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resolution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "response_id", nullable = false, unique = true)
    private Response response; // Melhor resposta escolhida

    @OneToOne
    @JoinColumn(name = "topic_id", nullable = false, unique = true)
    private Topic topic; // Tópico ao qual a resolução pertence
}
