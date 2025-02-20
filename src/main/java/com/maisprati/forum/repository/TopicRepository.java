package com.maisprati.forum.repository;

import com.maisprati.forum.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    // Adicionar metodo paginado
    Page<Topic> findAll(Pageable pageable);
}
