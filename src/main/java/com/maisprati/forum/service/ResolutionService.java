package com.maisprati.forum.service;

import com.maisprati.forum.exception.InvalidResolutionException;
import com.maisprati.forum.model.Resolution;
import com.maisprati.forum.model.Response;
import com.maisprati.forum.model.Topic;
import com.maisprati.forum.repository.ResolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResolutionService {

    @Autowired
    private ResolutionRepository resolutionRepository;

    // 🔹 Buscar a melhor resposta de um tópico
    public Optional<Resolution> getResolutionByTopicId(Long topicId) {
        return resolutionRepository.findByTopicId(topicId);
    }

    // 🔹 Escolher a melhor resposta para um tópico
    public Resolution saveResolution(Topic topic, Response response) {
        if (topic == null || response == null) {
            throw new IllegalArgumentException("Tópico e resposta não podem ser nulos.");
        }

        Optional<Resolution> existingResolution = resolutionRepository.findByTopicId(topic.getId());
        if (existingResolution.isPresent()) {
            throw new InvalidResolutionException("Este tópico já tem uma melhor resposta escolhida.");
        }

        Resolution resolution = new Resolution();
        resolution.setTopic(topic);
        resolution.setResponse(response);
        return resolutionRepository.save(resolution);
    }
}
