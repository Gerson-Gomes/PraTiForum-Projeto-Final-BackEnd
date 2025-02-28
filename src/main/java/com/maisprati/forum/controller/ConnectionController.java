package com.maisprati.forum.controller;

import com.maisprati.forum.dto.request.ConnectionDto;
import com.maisprati.forum.exception.ConnectionException;
import com.maisprati.forum.exception.UserNotFoundException;
import com.maisprati.forum.model.Connection;
import com.maisprati.forum.model.User;
import com.maisprati.forum.service.ConnectionService;
import com.maisprati.forum.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springdoc.core.annotations.ParameterObject;
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
    @Operation(summary = "Obter conexões de um usuário com paginação",
            description = "Retorna uma lista paginada das conexões de um usuário.",
            parameters = {
                    @Parameter(name = "page", description = "Número da página (inicia em 0)", schema = @Schema(type = "integer", defaultValue = "0")),
                    @Parameter(name = "size", description = "Quantidade de registros por página", schema = @Schema(type = "integer", defaultValue = "10"))
            })
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Connection>> getConnectionsByUser(@PathVariable Long userId, @ParameterObject Pageable pageable) {
        Optional<User> userOptional = connectionService.findUserById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = userOptional.get();
        Page<Connection> connections = connectionService.getConnectionsByUser(user, pageable);
        return ResponseEntity.ok(connections);
    }

    // Método para seguir um usuário
    @PostMapping("/follow")
    public ResponseEntity<Connection> followUser(@RequestBody ConnectionDto connectionDto) {
        User follower = userService.findById(connectionDto.getFollowerId())
                .orElseThrow(() -> new UserNotFoundException("Usuário seguidor não encontrado."));
        User followed = userService.findById(connectionDto.getFollowedId())
                .orElseThrow(() -> new UserNotFoundException("Usuário seguido não encontrado."));

        if (follower.getId().equals(followed.getId())) {
            throw new ConnectionException("Você não pode seguir a si mesmo.");
        }

        if (connectionService.isFollowing(follower, followed)) {
            throw new ConnectionException("Você já está seguindo este usuário.");
        }

        Connection newConnection = connectionService.followUser(follower, followed);
        return ResponseEntity.status(HttpStatus.CREATED).body(newConnection);
    }

    // Método para desfazer o "follow" (deixar de seguir)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> unfollowUser(@PathVariable Long id) {
        connectionService.unfollowUser(id);
        return ResponseEntity.noContent().build();
    }
}
