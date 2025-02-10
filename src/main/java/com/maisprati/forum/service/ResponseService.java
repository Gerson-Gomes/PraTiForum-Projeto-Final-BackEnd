package com.maisprati.forum.service;

import com.maisprati.forum.exception.ResponseNotFoundException;
import com.maisprati.forum.model.Response;
import com.maisprati.forum.repository.ResponseRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ResponseService {
    @Autowired
    private ResponseRepository responseRepository;

    public List<Response> getAllResponses() {
        return responseRepository.findAll();
    }

    public Response getResponseById(Long id) {
        if (id == null || id < 1) {
            throw new IllegalArgumentException("ID da resposta deve ser um número válido.");
        }
        return responseRepository.findById(id)
                .orElseThrow(() -> new ResponseNotFoundException("Resposta com ID " + id + " não encontrada."));
    }

    @Transactional
    public Response saveResponse(@Valid Response response) {
        if (response.getId() != null && responseRepository.existsById(response.getId())) {
            throw new IllegalArgumentException("Uma resposta com este ID já existe.");
        }
        return responseRepository.save(response);
    }

    public void deleteResponse(Long id) {
        if (!responseRepository.existsById(id)) {
            throw new ResponseNotFoundException("Não é possível excluir. Resposta com ID " + id + " não encontrada.");
        }
        responseRepository.deleteById(id);
    }
}
