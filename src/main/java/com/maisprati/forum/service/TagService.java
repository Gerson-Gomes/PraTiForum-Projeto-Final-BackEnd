package com.maisprati.forum.service;

import com.maisprati.forum.dto.TagDto;
import com.maisprati.forum.model.Tag;
import com.maisprati.forum.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public TagDto createTag(TagDto tagDto) {
        Tag tag = new Tag();
        tag.setName(tagDto.getName());
        Tag createdTag = tagRepository.save(tag);

        TagDto createdTagDto = new TagDto();
        createdTagDto.setId(createdTag.getId());
        createdTagDto.setName(createdTag.getName());
        return createdTagDto;
    }

    public List<TagDto> getAllTags() {
        return tagRepository.findAll().stream().map(tag -> {
            TagDto tagDto = new TagDto();
            tagDto.setId(tag.getId());
            tagDto.setName(tag.getName());
            return tagDto;
        }).collect(Collectors.toList());
    }

    public TagDto updateTag(Long id, TagDto tagDto) {
        Tag existingTag = tagRepository.findById(id).orElseThrow(() -> new RuntimeException("Tag não encontrada"));
        existingTag.setName(tagDto.getName());
        Tag updatedTag = tagRepository.save(existingTag);

        TagDto updatedTagDto = new TagDto();
        updatedTagDto.setId(updatedTag.getId());
        updatedTagDto.setName(updatedTag.getName());
        return updatedTagDto;
    }

    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }
}
