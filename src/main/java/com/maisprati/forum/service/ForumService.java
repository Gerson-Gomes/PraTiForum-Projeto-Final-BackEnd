package com.maisprati.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.maisprati.forum.repository.ForumRepository;
import com.maisprati.forum.model.Forum;

@Service
public class ForumService {

    @Autowired
    private ForumRepository forumRepository;

    public String getForumDetails() {
        // Implementar a lógica de negócio aqui
        return "Detalhes do fórum";
    }
}
