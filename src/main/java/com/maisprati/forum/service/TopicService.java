package com.maisprati.forum.service;

import com.maisprati.forum.model.Like;
import com.maisprati.forum.model.Topic;
import com.maisprati.forum.model.Response;
import com.maisprati.forum.repository.TopicRepository;
import com.maisprati.forum.repository.LikeRepository;
import com.maisprati.forum.repository.ResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private ResponseRepository responseRepository;

    public Topic createTopic(Topic topic) {
        return topicRepository.save(topic);
    }

    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public ResponseEntity<?> addResponse(Long topicId, Response response) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new RuntimeException("Tópico não encontrado"));
        response.setTopic(topic);
        responseRepository.save(response);
        return ResponseEntity.ok("Resposta adicionada com sucesso!");
    }

    public Topic updateTopic(Long topicId, Topic topic) {
        Topic existingTopic = topicRepository.findById(topicId).orElseThrow(() -> new RuntimeException("Tópico não encontrado"));
        existingTopic.setTitle(topic.getTitle());
        existingTopic.setContent(topic.getContent());
        return topicRepository.save(existingTopic);
    }

    public void deleteTopic(Long topicId) {
        topicRepository.deleteById(topicId);
    }
}
