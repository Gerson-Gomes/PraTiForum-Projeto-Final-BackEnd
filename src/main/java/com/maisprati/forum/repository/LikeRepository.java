package com.maisprati.forum.repository;

import com.maisprati.forum.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Long countByTopicId(Long topicId);
}
