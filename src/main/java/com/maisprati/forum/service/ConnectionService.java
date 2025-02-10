package com.maisprati.forum.service;

import com.maisprati.forum.dto.request.ConnectionDto;
import com.maisprati.forum.exception.ConnectionNotFoundException;
import com.maisprati.forum.exception.UserNotFoundException;
import com.maisprati.forum.model.Connection;
import com.maisprati.forum.model.User;
import com.maisprati.forum.repository.ConnectionRepository;
import com.maisprati.forum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final UserRepository userRepository;

    public List<Connection> getAllConnections() {
        return connectionRepository.findAll();
    }

    public Connection getConnectionById(Long id) {
        return connectionRepository.findById(id)
                .orElseThrow(() -> new ConnectionNotFoundException("Conexão não encontrada com id: " + id));
    }

    public Page<Connection> getConnectionsByUser(User user, Pageable pageable) {
        return connectionRepository.findByFollowerOrFollowed(user, user, pageable);
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com id: " + userId));
    }

    public Connection followUser(User follower, User followed) {
        Connection connection = new Connection();
        connection.setFollower(follower);
        connection.setFollowed(followed);

        return connectionRepository.save(connection);
    }

    public void unfollowUser(Long id) {
        if (!connectionRepository.existsById(id)) {
            throw new ConnectionNotFoundException("Conexão não encontrada com id: " + id);
        }
        connectionRepository.deleteById(id);
    }
}
