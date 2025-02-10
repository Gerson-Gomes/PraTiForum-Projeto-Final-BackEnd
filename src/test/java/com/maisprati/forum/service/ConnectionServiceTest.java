package com.maisprati.forum.service;

import com.maisprati.forum.exception.ConnectionNotFoundException;
import com.maisprati.forum.exception.UserNotFoundException;
import com.maisprati.forum.repository.ConnectionRepository;
import com.maisprati.forum.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class ConnectionServiceTest {

    @Mock
    private ConnectionRepository connectionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ConnectionService connectionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConnectionNotFoundException() {
        when(connectionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ConnectionNotFoundException.class, () -> connectionService.getConnectionById(1L));
    }

    @Test
    void testUserNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> connectionService.findUserById(1L));
    }
}
