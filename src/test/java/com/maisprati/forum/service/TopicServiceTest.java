package com.maisprati.forum.service;

import com.maisprati.forum.dto.request.TopicRegisterDto;
import com.maisprati.forum.exception.TopicNotFoundException;
import com.maisprati.forum.exception.UnauthorizedException;
import com.maisprati.forum.model.Topic;
import com.maisprati.forum.model.User;
import com.maisprati.forum.repository.TopicRepository;
import com.maisprati.forum.repository.UserRepository;
import com.maisprati.forum.utils.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class TopicServiceTest {

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private TopicService topicService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTopicNotFoundException() {
        when(topicRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(TopicNotFoundException.class, () -> topicService.getTopicById(1L));
    }

    @Test
    void testUnauthorizedException() {
        User user = new User();
        user.setId(1L);
        Topic topic = new Topic();
        topic.setId(1L);
        topic.setUser(user);

        when(topicRepository.findById(anyLong())).thenReturn(Optional.of(topic));
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.of(new User()));

        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(tokenService.extractUsername(anyString())).thenReturn("username");

        assertThrows(UnauthorizedException.class, () -> topicService.updateTopic(1L, new TopicRegisterDto(), request));
    }
}
