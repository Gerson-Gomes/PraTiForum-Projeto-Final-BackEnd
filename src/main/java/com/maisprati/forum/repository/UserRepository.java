package com.maisprati.forum.repository;

import com.maisprati.forum.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
    Optional<User> findByEmail(String email);
    Optional<User> findByRefreshToken(String refreshToken);

    // Adicionando metodo paginado
    Page<User> findAll(Pageable pageable);
}
