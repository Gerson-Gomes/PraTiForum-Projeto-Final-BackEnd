package com.maisprati.forum.controller;

import com.maisprati.forum.dto.response.LikeResponseDto;
import com.maisprati.forum.dto.response.TopicResponseDto;
import com.maisprati.forum.dto.request.TopicRegisterDto;
import com.maisprati.forum.dto.response.FavoriteTopicResponseDto;
import com.maisprati.forum.model.Like;
import com.maisprati.forum.service.TopicService;
import com.maisprati.forum.service.UserService;
import com.maisprati.forum.utils.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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

    @Operation(
            summary = "Lista tópicos paginados",
            description = "Retorna uma lista paginada de tópicos.\n\n" +
                    "Parâmetros de consulta:\n" +
                    "- **page**: Número da página (inicia em 0). Exemplo: 0\n" +
                    "- **size**: Quantidade de registros por página. Exemplo: 10\n" +
                    "- **sort**: Critério de ordenação no formato `campo,direction`, onde `direction` pode ser `asc` ou `desc`. Exemplo: `title,asc`"
    )
    @GetMapping
    public ResponseEntity<Page<TopicResponseDto>> getAllTopics(
            @ParameterObject Pageable pageable) {
        Page<TopicResponseDto> topicDtos = topicService.getAllTopics(pageable);
        return ResponseEntity.ok(topicDtos);
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
    public ResponseEntity<LikeResponseDto> likeTopic(@PathVariable Long id, HttpServletRequest httpServletRequest){
        String  token = tokenService.getTokenFromRequest(httpServletRequest);
        LikeResponseDto like = topicService.likeTopic(id, token);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{topics}")
                .buildAndExpand(like.getLikeId())
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
