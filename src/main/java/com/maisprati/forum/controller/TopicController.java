package com.maisprati.forum.controller;

import com.maisprati.forum.dto.TopicDto;
import com.maisprati.forum.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @PostMapping
    public ResponseEntity<TopicDto> createTopic(@RequestBody TopicDto topicDto) {
        TopicDto createdTopicDto = topicService.createTopic(topicDto);
        return ResponseEntity.ok(createdTopicDto);
    }

    @GetMapping
    public ResponseEntity<List<TopicDto>> getAllTopics() {
        List<TopicDto> topicDtos = topicService.getAllTopics();
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
