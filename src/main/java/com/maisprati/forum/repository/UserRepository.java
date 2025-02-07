package com.maisprati.forum.repository;

import com.maisprati.forum.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
    Optional<User> findByEmail(String email); // Adicionando o metodo findByEmail
    Optional<User> findByRefreshToken(String refreshToken); // Adicionando o metodo findByRefreshToken
}
