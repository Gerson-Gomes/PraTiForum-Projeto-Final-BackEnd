package com.maisprati.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.maisprati.forum.model.Forum;

public interface ForumRepository extends JpaRepository<Forum, Long> {
}
