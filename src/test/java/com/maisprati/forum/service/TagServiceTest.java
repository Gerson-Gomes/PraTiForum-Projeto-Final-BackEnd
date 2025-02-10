package com.maisprati.forum.service;

import com.maisprati.forum.dto.TagDto;
import com.maisprati.forum.exception.TagAlreadyExistsException;
import com.maisprati.forum.exception.TagNotFoundException;
import com.maisprati.forum.model.Tag;
import com.maisprati.forum.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTagAlreadyExistsException() {
        TagDto tagDto = new TagDto();
        tagDto.setName("existingtag");

        when(tagRepository.findTagByName(anyString())).thenReturn(Optional.of(new Tag()));

        assertThrows(TagAlreadyExistsException.class, () -> tagService.createTag(tagDto));
    }

    @Test
    void testTagNotFoundException() {
        when(tagRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> tagService.updateTag(1L, new TagDto()));
    }
}
