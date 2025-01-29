package com.maisprati.forum.repository;

import com.maisprati.forum.model.Connection;
import com.maisprati.forum.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {

    // Verifica se já existe uma conexão entre um seguidor e um seguido
    boolean existsByFollowerAndFollowed(User follower, User followed);

    // Encontra uma conexão específica entre um seguidor e um seguido
    Optional<Connection> findByFollowerAndFollowed(User follower, User followed);

    // Encontra todas as conexões onde o usuário é o seguidor
    List<Connection> findAllByFollower(User follower);

    // Encontra todas as conexões onde o usuário é o seguido
    List<Connection> findAllByFollowed(User followed);

    // Método para encontrar conexões de um usuário (como seguidor ou seguido) com paginação
    Page<Connection> findByFollowerOrFollowed(User follower, User followed, Pageable pageable);
}
