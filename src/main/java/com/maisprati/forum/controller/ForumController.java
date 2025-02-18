package com.maisprati.forum.controller;

import com.maisprati.forum.dto.request.ForumDto;
import com.maisprati.forum.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForumController {

    @Autowired
    private ForumService forumService;

    @GetMapping("/forum")
    public ForumDto getForum() {
        return forumService.getForumDetails();
    }

    @GetMapping("/forums")
    public Page<ForumDto> getAllForums(Pageable pageable) {
        return forumService.getAllForums(pageable);
    }
}
