package com.maisprati.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.maisprati.forum.model.Forum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ForumRepository extends JpaRepository<Forum, Long> {
    // Adicionar metodo paginado
    Page<Forum> findAll(Pageable pageable);
}
