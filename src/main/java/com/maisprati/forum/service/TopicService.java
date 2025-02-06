package com.maisprati.forum.service;


import com.maisprati.forum.dto.response.TopicResponseDto;
import com.maisprati.forum.dto.request.TopicRegisterDto;
import com.maisprati.forum.dto.response.FavoriteTopicResponseDto;
import com.maisprati.forum.model.Topic;
import com.maisprati.forum.model.Response;
import com.maisprati.forum.model.Tag;
import com.maisprati.forum.model.User;
import com.maisprati.forum.repository.*;
import com.maisprati.forum.service.token.TokenService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Topic createTopic(TopicRegisterDto topicDto, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        User user = userRepository.findByUserName(tokenService.extractUsername(token)).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        Tag tag = tagRepository.findById(topicDto.getTadId()).orElseThrow(() -> new EntityNotFoundException("Tag não encontrada."));
        List<Tag> listTag = new ArrayList<>();
        Topic topic = new Topic();

        listTag.add(tag);
        topic.setTitle(topicDto.getTitle());
        topic.setContent(topicDto.getContent());
        topic.setCreationDate(LocalDateTime.now());
        topic.setTags(listTag);
        topic.setUser(user);

        return topicRepository.save(topic);
    }

    @Transactional
    public TopicResponseDto getTopicById(Long id){
        return topicRepository.findById(id).map(TopicResponseDto::new)
                .orElseThrow(() -> new EntityNotFoundException("Tópico não encontrado com id: " + id)
                );
    }

    @Transactional
    public List<TopicResponseDto> getAllTopics() {
        return topicRepository.findAll().stream()
                .map(TopicResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public TopicResponseDto updateTopic(Long topicId, TopicResponseDto topicResponseDto) {
        Topic existingTopic = topicRepository.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Tópico não encontrado com id: " + topicId));

        if (topicResponseDto.getTitle() != null) {
            existingTopic.setTitle(topicResponseDto.getTitle());
        }
        if (topicResponseDto.getContent() != null) {
            existingTopic.setContent(topicResponseDto.getContent());
        }

        // Tratamento seguro para tags (null-safe e busca com exceção específica)
        List<Long> tagIds = Optional.ofNullable(topicResponseDto.getTagIds()).orElse(Collections.emptyList());
        List<Tag> tags = tagIds.stream()
                .map(tagId -> tagRepository.findById(tagId)
                        .orElseThrow(() -> new EntityNotFoundException("Tag não encontrada com id: " + tagId)))
                .collect(Collectors.toList());
        existingTopic.setTags(tags);

        return new TopicResponseDto(topicRepository.save(existingTopic));
    }

    @Transactional
    public void deleteTopic(Long topicId, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        User user = userRepository.findByUserName(tokenService.extractUsername(token)).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        Topic existingTopic = topicRepository.findById(topicId).orElseThrow(() -> new EntityNotFoundException("Topic não encontrado"));

        if(existingTopic.getUser().equals(user)){
            topicRepository.deleteById(topicId);
        }
        else throw new SecurityException("Usuário não tem direito de apagar o tópico de outro usuário.");
        ResponseEntity.noContent().build();
    }

    @Transactional
    public FavoriteTopicResponseDto favoriteTopic(Long id, HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        String username = tokenService.extractUsername(token);
        User user = userRepository.findByUserName(username).orElseThrow();

        Topic topic = topicRepository.findById(id) .orElseThrow(
                () -> new EntityNotFoundException("Topic not found with id " + id));
        List<User> topicUserFavorite = topic.getUsersWhoFavorited();
        topicUserFavorite.add(user);
        topic.setUsersWhoFavorited(topicUserFavorite);
        topicRepository.save(topic);

        return new FavoriteTopicResponseDto(id,user.getId());
    }

    @Transactional
    public FavoriteTopicResponseDto unfavoriteTopic(Long id, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = tokenService.extractUsername(token);
        User user = userRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found with id " + id));

        List<User> topicUserFavorite = topic.getUsersWhoFavorited();
        topicUserFavorite.remove(user);
        topic.setUsersWhoFavorited(topicUserFavorite);
        topicRepository.save(topic);

        return new FavoriteTopicResponseDto(id, user.getId());
    }

    public ResponseEntity<?> addResponse(Long topicId, Response response) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new RuntimeException("Tópico não encontrado"));
        response.setTopic(topic);
        responseRepository.save(response);
        return ResponseEntity.ok("Resposta adicionada com sucesso!");
    }
}
