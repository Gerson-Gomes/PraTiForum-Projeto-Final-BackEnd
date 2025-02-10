package com.maisprati.forum.service;

import com.maisprati.forum.dto.request.TopicRegisterDto;
import com.maisprati.forum.dto.response.FavoriteTopicResponseDto;
import com.maisprati.forum.dto.response.TopicResponseDto;
import com.maisprati.forum.exception.TopicNotFoundException;
import com.maisprati.forum.exception.UnauthorizedException;
import com.maisprati.forum.model.Response;
import com.maisprati.forum.model.Tag;
import com.maisprati.forum.model.Topic;
import com.maisprati.forum.model.User;
import com.maisprati.forum.repository.ResponseRepository;
import com.maisprati.forum.repository.TagRepository;
import com.maisprati.forum.repository.TopicRepository;
import com.maisprati.forum.repository.UserRepository;
import com.maisprati.forum.service.token.TokenService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final ResponseRepository responseRepository;
    private final TokenService tokenService;

    public TopicResponseDto createTopic(TopicRegisterDto topicDto, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        User user = userRepository.findByUserName(tokenService.extractUsername(token))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        Tag tag = tagRepository.findById(topicDto.getTadId())
                .orElseThrow(() -> new EntityNotFoundException("Tag não encontrada."));

        Topic topic = new Topic();
        topic.setTitle(topicDto.getTitle());
        topic.setContent(topicDto.getContent());
        topic.setCreationDate(LocalDateTime.now());
        topic.setTags(Collections.singletonList(tag));
        topic.setUser(user);

        Topic createdTopic = topicRepository.save(topic);
        return new TopicResponseDto(createdTopic);
    }

    @Transactional
    public List<TopicResponseDto> getAllTopics() {
        return topicRepository.findAll().stream()
                .map(TopicResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public TopicResponseDto getTopicById(Long id) {
        return topicRepository.findById(id)
                .map(TopicResponseDto::new)
                .orElseThrow(() -> new TopicNotFoundException("Tópico não encontrado com id: " + id));
    }

    @Transactional
    public TopicResponseDto updateTopic(Long topicId, TopicRegisterDto topicRegisterDto, HttpServletRequest request) {
        Topic existingTopic = topicRepository.findById(topicId)
                .orElseThrow(() -> new TopicNotFoundException("Tópico não encontrado com id: " + topicId));

        String token = request.getHeader("Authorization").substring(7);
        User userToken = userRepository.findByUserName(tokenService.extractUsername(token))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        if (!Objects.equals(userToken.getId(), existingTopic.getUser().getId())) {
            throw new UnauthorizedException("Impossível editar um Tópico que não é seu.");
        }

        if (topicRegisterDto.getTitle() != null) {
            existingTopic.setTitle(topicRegisterDto.getTitle());
        }
        if (topicRegisterDto.getContent() != null) {
            existingTopic.setContent(topicRegisterDto.getContent());
        }

        Optional<Long> tagId = Optional.ofNullable(topicRegisterDto.getTadId());
        List<Tag> tags = tagId.map(id -> tagRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Tag não encontrada com id: " + id)))
                .map(Collections::singletonList)
                .orElse(Collections.emptyList());
        existingTopic.setTags(tags);

        return new TopicResponseDto(topicRepository.save(existingTopic));
    }

    @Transactional
    public void deleteTopic(Long topicId, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        User user = userRepository.findByUserName(tokenService.extractUsername(token))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        Topic existingTopic = topicRepository.findById(topicId)
                .orElseThrow(() -> new TopicNotFoundException("Tópico não encontrado"));

        if (!existingTopic.getUser().equals(user)) {
            throw new UnauthorizedException("Usuário não tem direito de apagar o tópico de outro usuário.");
        }

        topicRepository.deleteById(topicId);
    }

    @Transactional
    public FavoriteTopicResponseDto favoriteTopic(Long id, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        User user = userRepository.findByUserName(tokenService.extractUsername(token))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new TopicNotFoundException("Tópico não encontrado com id: " + id));

        topic.getUsersWhoFavorited().add(user);
        topicRepository.save(topic);

        return new FavoriteTopicResponseDto(id, user.getId());
    }

    @Transactional
    public FavoriteTopicResponseDto unfavoriteTopic(Long id, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        User user = userRepository.findByUserName(tokenService.extractUsername(token))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new TopicNotFoundException("Tópico não encontrado com id: " + id));

        topic.getUsersWhoFavorited().remove(user);
        topicRepository.save(topic);

        return new FavoriteTopicResponseDto(id, user.getId());
    }

    public ResponseEntity<?> addResponse(Long topicId, Response response) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new TopicNotFoundException("Tópico não encontrado"));

        response.setTopic(topic);
        responseRepository.save(response);

        return ResponseEntity.ok("Resposta adicionada com sucesso!");
    }
}
