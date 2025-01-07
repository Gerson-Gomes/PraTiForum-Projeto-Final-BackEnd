package com.maisprati.forum.controller;

import com.maisprati.forum.model.Topic;
import com.maisprati.forum.model.Response;
import com.maisprati.forum.service.TopicService;
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
    public ResponseEntity<Topic> createTopic(@RequestBody Topic topic) {
        return ResponseEntity.ok(topicService.createTopic(topic));
    }

    @GetMapping
    public ResponseEntity<List<Topic>> getAllTopics() {
        return ResponseEntity.ok(topicService.getAllTopics());
    }

    @PostMapping("/{id}/responses")
    public ResponseEntity<?> addResponse(@PathVariable Long id, @RequestBody Response response) {
        return topicService.addResponse(id, response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Topic> updateTopic(@PathVariable Long id, @RequestBody Topic topic) {
        return ResponseEntity.ok(topicService.updateTopic(id, topic));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTopic(@PathVariable Long id) {
        topicService.deleteTopic(id);
        return ResponseEntity.ok("Tópico deletado com sucesso!");
    }
}
