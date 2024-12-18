package com.maisprati.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.maisprati.forum.service.ForumService;

@RestController
public class ForumController {

    @Autowired
    private ForumService forumService;

    @GetMapping("/forum")
    public String getForum() {
        return forumService.getForumDetails();
    }
}
