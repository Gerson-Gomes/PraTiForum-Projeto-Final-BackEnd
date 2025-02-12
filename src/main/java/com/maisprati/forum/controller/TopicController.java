package com.maisprati.forum.controller;

import com.maisprati.forum.dto.response.TopicResponseDto;
import com.maisprati.forum.dto.request.TopicRegisterDto;
import com.maisprati.forum.dto.response.FavoriteTopicResponseDto;
import com.maisprati.forum.model.Like;
import com.maisprati.forum.service.TopicService;
import com.maisprati.forum.service.UserService;
import com.maisprati.forum.utils.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<TopicResponseDto> createTopic(@RequestBody TopicRegisterDto topicDto, HttpServletRequest request) {
        TopicResponseDto createdTopicDto = topicService.createTopic(topicDto, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{topics}")
                .buildAndExpand(createdTopicDto.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdTopicDto);
    }

    @GetMapping
    public ResponseEntity<List<TopicResponseDto>> getAllTopics() {
        List<TopicResponseDto> topicDtos = topicService.getAllTopics();
        return ResponseEntity.ok().body(topicDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicResponseDto> getTopicById(@PathVariable Long id) {
        TopicResponseDto topicDto = topicService.getTopicById(id);
        return ResponseEntity.ok().body(topicDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicResponseDto> updateTopic(@PathVariable Long id,
                                                        @RequestBody TopicRegisterDto topicRegisterDto,
                                                        HttpServletRequest request) {
        TopicResponseDto updatedTopicDto = topicService.updateTopic(id, topicRegisterDto, request);
        return ResponseEntity.ok().body(updatedTopicDto);
    }

    @PostMapping("/favorite/{id}")
    public ResponseEntity<FavoriteTopicResponseDto> favoriteTopicById(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        FavoriteTopicResponseDto favoriteTopicDto = topicService.favoriteTopic(id, httpServletRequest);
        return ResponseEntity.ok().body(favoriteTopicDto);
    }

    @DeleteMapping("/unfavorite/{id}")
    public ResponseEntity<FavoriteTopicResponseDto> unfavoriteTopicById(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        topicService.unfavoriteTopic(id, httpServletRequest);
        return ResponseEntity.noContent().build();    }

    @PostMapping("/like/{id}")
    public ResponseEntity<Like> likeTopic(@PathVariable Long id, HttpServletRequest httpServletRequest){
        String  token = tokenService.getTokenFromRequest(httpServletRequest);
        Like like = topicService.likeTopic(id, token);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{topics}")
                .buildAndExpand(like.getId())
                .toUri();

        return ResponseEntity.created(location).body(like);
    }

    @DeleteMapping("/unlike/{id}")
    public ResponseEntity<Void> unlikeTopic(@PathVariable Long id, HttpServletRequest request) {
        String token = tokenService.getTokenFromRequest(request);
        topicService.unlikeTopic(id, token);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id, HttpServletRequest request) {
        topicService.deleteTopic(id, request);
        return ResponseEntity.noContent().build();
    }


}
