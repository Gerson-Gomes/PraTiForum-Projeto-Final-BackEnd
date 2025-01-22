package com.maisprati.forum.service;

import com.maisprati.forum.dto.TopicDto;
import com.maisprati.forum.model.Topic;
import com.maisprati.forum.model.Response;
import com.maisprati.forum.repository.TopicRepository;
import com.maisprati.forum.repository.LikeRepository;
import com.maisprati.forum.repository.ResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private ResponseRepository responseRepository;

    public TopicDto createTopic(TopicDto topicDto) {
        Topic topic = new Topic();
        topic.setTitle(topicDto.getTitle());
        topic.setContent(topicDto.getContent());
        Topic createdTopic = topicRepository.save(topic);

        TopicDto createdTopicDto = new TopicDto();
        createdTopicDto.setId(createdTopic.getId());
        createdTopicDto.setTitle(createdTopic.getTitle());
        createdTopicDto.setContent(createdTopic.getContent());

        return createdTopicDto;
    }

    public List<TopicDto> getAllTopics() {
        return topicRepository.findAll().stream().map(topic -> {
            TopicDto topicDto = new TopicDto();
            topicDto.setId(topic.getId());
            topicDto.setTitle(topic.getTitle());
            topicDto.setContent(topic.getContent());
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
        Topic existingTopic = topicRepository.findById(topicId).orElseThrow(() -> new RuntimeException("Tópico não encontrado"));
        existingTopic.setTitle(topicDto.getTitle());
        existingTopic.setContent(topicDto.getContent());
        Topic updatedTopic = topicRepository.save(existingTopic);

        TopicDto updatedTopicDto = new TopicDto();
        updatedTopicDto.setId(updatedTopic.getId());
        updatedTopicDto.setTitle(updatedTopic.getTitle());
        updatedTopicDto.setContent(updatedTopic.getContent());

        return updatedTopicDto;
    }

    public void deleteTopic(Long topicId) {
        topicRepository.deleteById(topicId);
    }
}
