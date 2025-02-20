package com.maisprati.forum.service;

import com.maisprati.forum.dto.request.ForumDto;
import com.maisprati.forum.model.Forum;
import com.maisprati.forum.repository.ForumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ForumService {

    private final ForumRepository forumRepository;

    public ForumDto getForumDetails() {
        ForumDto forumDto = new ForumDto();
        forumDto.setId(1L);
        forumDto.setName("Forum Example");
        forumDto.setDescription("Description of the forum");
        return forumDto;
    }

    public Page<ForumDto> getAllForums(Pageable pageable) {
        Page<Forum> forums = forumRepository.findAll(pageable);
        return forums.map(forum -> {
            ForumDto dto = new ForumDto();
            dto.setId(forum.getId());
            dto.setName(forum.getName());
            dto.setDescription(forum.getDescription());
            return dto;
        });
    }
}
