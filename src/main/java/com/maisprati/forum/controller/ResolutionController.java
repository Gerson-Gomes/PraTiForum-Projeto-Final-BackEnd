package com.maisprati.forum.controller;

import com.maisprati.forum.dto.response.ResolutionDto;
import com.maisprati.forum.model.Resolution;
import com.maisprati.forum.model.Response;
import com.maisprati.forum.model.Topic;
import com.maisprati.forum.service.ResolutionService;
import com.maisprati.forum.service.ResponseService;
import com.maisprati.forum.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
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
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔹 Escolher a melhor resposta para um tópico
    @PostMapping
    public ResponseEntity<ResolutionDto> chooseBestResponse(@RequestParam Long topicId, @RequestParam Long responseId) {
        Optional<Topic> topicOptional = Optional.ofNullable(topicService.getTopicById(topicId));
        Response response = responseService.getResponseById(responseId);

        if (topicOptional.isEmpty() || response == null) {
            return ResponseEntity.badRequest().build();
        }

        Resolution resolution = resolutionService.saveResolution(topicOptional.get(), response);
        return ResponseEntity.ok(new ResolutionDto(resolution));
    }
}
