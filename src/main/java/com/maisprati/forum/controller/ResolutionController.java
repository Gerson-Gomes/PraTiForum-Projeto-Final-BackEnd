package com.maisprati.forum.controller;

import com.maisprati.forum.dto.response.ResolutionDto;
import com.maisprati.forum.dto.response.TopicResponseDto;
import com.maisprati.forum.exception.ResponseNotBelongToTopicException;
import com.maisprati.forum.exception.TopicNotFoundException;
import com.maisprati.forum.model.Resolution;
import com.maisprati.forum.model.Response;
import com.maisprati.forum.model.Topic;
import com.maisprati.forum.service.ResolutionService;
import com.maisprati.forum.service.ResponseService;
import com.maisprati.forum.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/resolutions")
public class ResolutionController {

    @Autowired
    private ResolutionService resolutionService;

    @Autowired
    private ResponseService responseService;

    @Autowired
    private TopicService topicService;

    // 🔹 Buscar a melhor resposta de um tópico
    @GetMapping("/{topicId}")
    public ResponseEntity<ResolutionDto> getResolutionByTopic(@PathVariable Long topicId) {
        Optional<Resolution> resolution = resolutionService.getResolutionByTopicId(topicId);
        return resolution.map(r -> ResponseEntity.ok(new ResolutionDto(r)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // 🔹 Escolher a melhor resposta para um tópico
    @PostMapping
    public ResponseEntity<?> chooseBestResponse(@RequestParam Long topicId, @RequestParam Long responseId) {
        try {
            Topic topic = topicService.findTopicById(topicId)
                    .orElseThrow(() -> new TopicNotFoundException("Tópico não encontrado."));

            Response response = responseService.getResponseById(responseId);

            if (!response.getTopic().getId().equals(topicId)) {
                throw new ResponseNotBelongToTopicException("A resposta não pertence a este tópico.");
            }

            Resolution resolution = resolutionService.saveResolution(topic, response);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResolutionDto(resolution));
        } catch (TopicNotFoundException | ResponseNotBelongToTopicException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
