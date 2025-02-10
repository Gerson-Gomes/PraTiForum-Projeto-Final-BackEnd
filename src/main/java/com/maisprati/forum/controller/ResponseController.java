package com.maisprati.forum.controller;

import com.maisprati.forum.dto.response.ResponseDto;
import com.maisprati.forum.exception.ResponseNotFoundException;
import com.maisprati.forum.model.Response;
import com.maisprati.forum.service.ResponseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        Response response = responseService.getResponseById(id);
        return ResponseEntity.ok(new ResponseDto(response));
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createResponse(@Valid @RequestBody Response response) {
        Response savedResponse = responseService.saveResponse(response);
        return ResponseEntity.ok(new ResponseDto(savedResponse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteResponse(@PathVariable Long id) {
        responseService.deleteResponse(id);
        return ResponseEntity.ok("Resposta deletada com sucesso.");
    }

    @ExceptionHandler(ResponseNotFoundException.class)
    public ResponseEntity<String> handleResponseNotFound(ResponseNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(400).body(ex.getMessage());
    }
}
