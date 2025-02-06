package com.maisprati.forum.controller;

import com.maisprati.forum.dto.response.ResponseDto;
import com.maisprati.forum.model.Response;
import com.maisprati.forum.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/responses")
public class ResponseController {
    @Autowired
    private ResponseService responseService;

    @GetMapping
    public List<ResponseDto> getAllResponses() {
        return responseService.getAllResponses()
                .stream()
                .map(ResponseDto::new)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getResponseById(@PathVariable Long id) {
        Optional<Response> response = responseService.getResponseById(id);
        return response.map(r -> ResponseEntity.ok(new ResponseDto(r)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createResponse(@RequestBody Response response) {
        Response savedResponse = responseService.saveResponse(response);
        return ResponseEntity.ok(new ResponseDto(savedResponse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResponse(@PathVariable Long id) {
        responseService.deleteResponse(id);
        return ResponseEntity.noContent().build();
    }
}
