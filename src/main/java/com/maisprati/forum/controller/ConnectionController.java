package com.maisprati.forum.controller;

import com.maisprati.forum.dto.request.ConnectionDto;
import com.maisprati.forum.dto.response.ConnectionResponseDto;
import com.maisprati.forum.exception.ConnectionNotFoundException;
import com.maisprati.forum.exception.UserNotFoundException;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/connections")
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<ConnectionResponseDto>> getAllConnections() {
        List<Connection> connections = connectionService.getAllConnections();
        List<ConnectionResponseDto> connectionDtos = connections.stream()
                .map(ConnectionResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(connectionDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConnectionResponseDto> getConnectionById(@PathVariable Long id) {
        try {
            Connection connection = connectionService.getConnectionById(id);
            return ResponseEntity.ok(new ConnectionResponseDto(connection));
        } catch (ConnectionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ConnectionResponseDto>> getConnectionsByUser(@PathVariable Long userId,
                                                                            @RequestParam int page,
                                                                            @RequestParam int size) {
        try {
            User user = connectionService.findUserById(userId);
            Pageable pageable = PageRequest.of(page, size);
            Page<Connection> connections = connectionService.getConnectionsByUser(user, pageable);
            Page<ConnectionResponseDto> connectionDtos = connections.map(ConnectionResponseDto::new);
            return ResponseEntity.ok(connectionDtos);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/follow")
    public ResponseEntity<ConnectionResponseDto> followUser(@RequestBody ConnectionDto connectionDto) {
        try {
            User follower = userService.findById(connectionDto.getFollowerId())
                    .orElseThrow(() -> new UserNotFoundException("Follower not found"));
            User followed = userService.findById(connectionDto.getFollowedId())
                    .orElseThrow(() -> new UserNotFoundException("Followed not found"));

            Connection newConnection = connectionService.followUser(follower, followed);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ConnectionResponseDto(newConnection));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> unfollowUser(@PathVariable Long id) {
        try {
            connectionService.unfollowUser(id);
            return ResponseEntity.noContent().build();
        } catch (ConnectionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
