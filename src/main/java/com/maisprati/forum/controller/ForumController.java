package com.maisprati.forum.controller;

import com.maisprati.forum.dto.ForumDto;
import com.maisprati.forum.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
