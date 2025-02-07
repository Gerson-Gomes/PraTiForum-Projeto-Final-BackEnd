package com.maisprati.forum.service;

import com.maisprati.forum.dto.request.ForumDto;
import com.maisprati.forum.repository.ForumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // Lombok cria o construtor com o campo final
public class ForumService {

    private final ForumRepository forumRepository;

    public ForumDto getForumDetails() {
        ForumDto forumDto = new ForumDto();
        forumDto.setId(1L);
        forumDto.setName("Forum Example");
        forumDto.setDescription("Description of the forum");
        return forumDto;
    }
}
