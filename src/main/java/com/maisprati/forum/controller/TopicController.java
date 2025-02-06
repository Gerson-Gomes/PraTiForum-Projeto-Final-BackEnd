package com.maisprati.forum.controller;

import com.maisprati.forum.dto.request.TopicDto;
import com.maisprati.forum.dto.request.TopicRegisterDto;
import com.maisprati.forum.dto.response.FavoriteTopicResponseDto;
import com.maisprati.forum.model.Topic;
import com.maisprati.forum.service.TopicService;
import com.maisprati.forum.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URL;
import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @PostMapping
    public ResponseEntity<Topic> createTopic(@RequestBody TopicRegisterDto topicDto,  HttpServletRequest request) {
        var createdTopicDto = topicService.createTopic(topicDto, request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{topics}")
                .buildAndExpand(createdTopicDto.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdTopicDto);
    }

    @GetMapping
    public ResponseEntity<List<TopicDto>> getAllTopics() {
        var topicDtos = topicService.getAllTopics();

        return ResponseEntity.ok().body(topicDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicDto> getTopicById(@PathVariable Long id) {
        var topicDtos = topicService.getTopicById(id);
        return ResponseEntity.ok().body(topicDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicDto> updateTopic(@PathVariable Long id, @RequestBody TopicDto topicDto) {
        var updatedTopicDto = topicService.updateTopic(id, topicDto);
        return ResponseEntity.ok().body(updatedTopicDto);
    }

    @PostMapping("/{id}/favorite")
    public ResponseEntity<FavoriteTopicResponseDto> favoriteTopicById(@PathVariable Long id, HttpServletRequest httpServletRequest){
        var favoriteTopicDto = topicService.favoriteTopic(id, httpServletRequest);
        return ResponseEntity.ok().body(favoriteTopicDto);
    }

    @PostMapping("/{id}/unfavorite")
    public ResponseEntity<FavoriteTopicResponseDto> unfavoriteTopicById(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        var favoriteTopicDto = topicService.unfavoriteTopic(id, httpServletRequest);
        return ResponseEntity.ok().body(favoriteTopicDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTopic(@PathVariable Long id, HttpServletRequest request) {
        topicService.deleteTopic(id, request);
        return ResponseEntity.noContent().build();
    }
}
