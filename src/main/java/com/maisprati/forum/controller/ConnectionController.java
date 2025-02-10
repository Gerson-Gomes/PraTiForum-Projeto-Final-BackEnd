package com.maisprati.forum.controller;

import com.maisprati.forum.dto.request.ConnectionDto;
import com.maisprati.forum.exceptions.ConnectionException;
import com.maisprati.forum.exceptions.UserNotFoundException;
import com.maisprati.forum.model.Connection;
import com.maisprati.forum.model.User;
import com.maisprati.forum.service.ConnectionService;
import com.maisprati.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/connections")
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private UserService userService;

    // Método para obter todas as conexões
    @GetMapping
    public ResponseEntity<List<Connection>> getAllConnections() {
        List<Connection> connections = connectionService.getAllConnections();
        return ResponseEntity.ok(connections);
    }

    // Método para obter uma conexão específica pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Connection> getConnectionById(@PathVariable Long id) {
        Optional<Connection> connection = connectionService.getConnectionById(id);

        return connection.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Método para obter conexões de um usuário com paginação
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Connection>> getConnectionsByUser(@PathVariable Long userId,
                                                                 @RequestParam int page,
                                                                 @RequestParam int size) {
        // Buscar o usuário pelo ID
        Optional<User> userOptional = connectionService.findUserById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = userOptional.get();

        // Configurar a paginação
        Pageable pageable = PageRequest.of(page, size);

        // Obtendo as conexões do usuário com paginação em casos de muitos seguidores.
        Page<Connection> connections = connectionService.getConnectionsByUser(user, pageable);

        // Retorna as conexões com status OK
        return ResponseEntity.ok(connections);
    }

    // Método para seguir um usuário
    @PostMapping("/follow")
    public ResponseEntity<Connection> followUser(@RequestBody ConnectionDto connectionDto) {
        // Buscar os usuários por ID no DTO
        User follower = userService.findById(connectionDto.getFollowerId())
                .orElseThrow(() -> new UserNotFoundException("Usuário seguidor não encontrado."));
        User followed = userService.findById(connectionDto.getFollowedId())
                .orElseThrow(() -> new UserNotFoundException("Usuário seguido não encontrado."));

        // Impedir que um usuário siga a si mesmo
        if (follower.getId().equals(followed.getId())) {
            throw new ConnectionException("Você não pode seguir a si mesmo.");
        }

        // Impedir que um usuário siga alguém que já está seguindo
        if (connectionService.isFollowing(follower, followed)) {
            throw new ConnectionException("Você já está seguindo este usuário.");
        }

        // Criar a nova conexão (seguir o usuário)
        Connection newConnection = connectionService.followUser(follower, followed);

        // Retorna a conexão criada com status CREATED
        return ResponseEntity.status(HttpStatus.CREATED).body(newConnection);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }


    // Método para desfazer o "follow" (deixar de seguir)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> unfollowUser(@PathVariable Long id) {
        connectionService.unfollowUser(id);
        return ResponseEntity.noContent().build();
    }
}
