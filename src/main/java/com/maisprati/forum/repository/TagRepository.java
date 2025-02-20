package com.maisprati.forum.repository;

import com.maisprati.forum.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findTagByName(String string);

    // Adicionar metodo paginado
    Page<Tag> findAll(Pageable pageable);
}
