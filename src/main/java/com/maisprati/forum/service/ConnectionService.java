package com.maisprati.forum.service;

import com.maisprati.forum.dto.request.ConnectionDto;
import com.maisprati.forum.model.Connection;
import com.maisprati.forum.model.User;
import com.maisprati.forum.repository.ConnectionRepository;
import com.maisprati.forum.repository.UserRepository;
import lombok.RequiredArgsConstructor; // Lombok para gerar construtor automaticamente
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // Lombok cria o construtor com os campos finais
public class ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final UserRepository userRepository;

    // Método para obter todas as conexões
    public List<Connection> getAllConnections() {
        return connectionRepository.findAll();
    }

    // Método para obter uma conexão específica pelo ID
    public Optional<Connection> getConnectionById(Long id) {
        return connectionRepository.findById(id);
    }

    // Método para obter as conexões de um usuário com paginação
    public Page<Connection> getConnectionsByUser(User user, Pageable pageable) {
        return connectionRepository.findByFollowerOrFollowed(user, user, pageable);
    }

    // Método para buscar um usuário pelo ID
    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    // Método para criar uma nova conexão (seguir um usuário)
    public Connection followUser(User follower, User followed) {
        Connection connection = new Connection();
        connection.setFollower(follower);
        connection.setFollowed(followed);

        return connectionRepository.save(connection);
    }

    // Método para desfazer a conexão (deixar de seguir)
    public void unfollowUser(Long id) {
        connectionRepository.deleteById(id);
    }
}
