package com.maisprati.forum.service;


import com.maisprati.forum.dto.request.TopicDto;
import com.maisprati.forum.dto.request.TopicRegisterDto;
import com.maisprati.forum.model.Topic;
import com.maisprati.forum.model.Response;
import com.maisprati.forum.model.Tag;
import com.maisprati.forum.model.User;
import com.maisprati.forum.repository.*;
import com.maisprati.forum.service.token.TokenService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    public Topic createTopic(TopicRegisterDto topicDto, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        User user = userRepository.findByUserName(tokenService.extractUsername(token));

        Optional<Tag> tag = tagRepository.findById(topicDto.getTadId());
        List<Tag> listTag = new ArrayList<>();
        Topic topic = new Topic();


        listTag.add(tag.get());
        topic.setTitle(topicDto.getTitle());
        topic.setContent(topicDto.getContent());
        topic.setCreationDate(LocalDateTime.now());

        topic.setTags(listTag);
        topic.setUser(user);


        return topicRepository.save(topic);
    }

    public List<TopicDto> getAllTopics() {
        return topicRepository.findAll().stream().map(topic -> {
            TopicDto topicDto = new TopicDto();
            topicDto.setId(topic.getId());
            topicDto.setTitle(topic.getTitle());
            topicDto.setContent(topic.getContent());
            topicDto.setTagIds(topic.getTags().stream().map(Tag::getId).collect(Collectors.toList()));
            return topicDto;
        }).collect(Collectors.toList());
    }

    public ResponseEntity<?> addResponse(Long topicId, Response response) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new RuntimeException("Tópico não encontrado"));
        response.setTopic(topic);
        responseRepository.save(response);
        return ResponseEntity.ok("Resposta adicionada com sucesso!");
    }

    public TopicDto updateTopic(Long topicId, TopicDto topicDto) {
        // Busca o tópico existente com tratamento adequado
        Topic existingTopic = topicRepository.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Tópico não encontrado com id: " + topicId));

        // Atualiza campos básicos com validação de null
        if (topicDto.getTitle() != null) {
            existingTopic.setTitle(topicDto.getTitle());
        }
        if (topicDto.getContent() != null) {
            existingTopic.setContent(topicDto.getContent());
        }

        // Tratamento seguro para tags (null-safe e busca com exceção específica)
        List<Long> tagIds = Optional.ofNullable(topicDto.getTagIds()).orElse(Collections.emptyList());
        List<Tag> tags = tagIds.stream()
                .map(tagId -> tagRepository.findById(tagId)
                        .orElseThrow(() -> new EntityNotFoundException("Tag não encontrada com id: " + tagId)))
                .collect(Collectors.toList());
        existingTopic.setTags(tags);

        // Persistência e conversão para DTO
        Topic updatedTopic = topicRepository.save(existingTopic);
        return convertToDto(updatedTopic);
    }

    private TopicDto convertToDto(Topic topic) {
        TopicDto dto = new TopicDto();
        dto.setId(topic.getId());
        dto.setTitle(topic.getTitle());
        dto.setContent(topic.getContent());
        dto.setTagIds(topic.getTags().stream().map(Tag::getId).collect(Collectors.toList()));
        return dto;
    }

    public void deleteTopic(Long topicId) {

        if (responseRepository.existsById(topicId)){
            topicRepository.deleteById(topicId);
        }
        ResponseEntity.noContent().build();
    }
}
