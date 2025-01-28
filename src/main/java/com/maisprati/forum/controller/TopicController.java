package com.maisprati.forum.controller;

import com.maisprati.forum.dto.request.TopicDto;
import com.maisprati.forum.dto.request.TopicRegisterDto;
import com.maisprati.forum.model.Topic;
import com.maisprati.forum.service.TopicService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @PostMapping
    public ResponseEntity<Topic> createTopic(@RequestBody TopicRegisterDto topicDto,  HttpServletRequest request) {
        Topic createdTopicDto = topicService.createTopic(topicDto, request);
        return ResponseEntity.ok(createdTopicDto);
    }

    @GetMapping
    public ResponseEntity<List<TopicDto>> getAllTopics() {
        List<TopicDto> topicDtos = topicService.getAllTopics();

        return ResponseEntity.ok(topicDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Topic> getTopicById(@PathVariable Long id) {
        Topic topicDtos = topicService.getTopicById(id);

        return ResponseEntity.ok(topicDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicDto> updateTopic(@PathVariable Long id, @RequestBody TopicDto topicDto) {
        TopicDto updatedTopicDto = topicService.updateTopic(id, topicDto);
        return ResponseEntity.ok(updatedTopicDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTopic(@PathVariable Long id) {
        topicService.deleteTopic(id);
        return ResponseEntity.ok("Tópico deletado com sucesso!");
    }
}
