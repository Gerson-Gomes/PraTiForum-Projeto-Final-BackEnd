package com.maisprati.forum.repository;

import com.maisprati.forum.model.Resolution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResolutionRepository extends JpaRepository<Resolution, Long> {
    Optional<Resolution> findByTopicId(Long topicId);
}
