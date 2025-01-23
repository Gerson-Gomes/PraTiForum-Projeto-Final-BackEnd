package com.maisprati.forum.repository;

import com.maisprati.forum.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
