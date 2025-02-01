package com.maisprati.forum.service;

import com.maisprati.forum.dto.request.ForumDto;
import com.maisprati.forum.repository.ForumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForumService {

    @Autowired
    private ForumRepository forumRepository;

    public ForumDto getForumDetails() {
        // Implementar a logica de negocio para obter os detalhes do forum
        ForumDto forumDto = new ForumDto();
        forumDto.setId(1L);
        forumDto.setName("Forum Example");
        forumDto.setDescription("Description of the forum");
        return forumDto;
    }
}