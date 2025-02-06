package com.maisprati.forum.service;

import com.maisprati.forum.model.Response;
import com.maisprati.forum.repository.ResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResponseService {
    @Autowired
    private ResponseRepository responseRepository;

    public List<Response> getAllResponses() {
        return responseRepository.findAll();
    }

    public Optional<Response> getResponseById(Long id) {
        return responseRepository.findById(id);
    }

    public Response saveResponse(Response response) {
        return responseRepository.save(response);
    }

    public void deleteResponse(Long id) {
        responseRepository.deleteById(id);
    }
}
