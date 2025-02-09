package com.maisprati.forum.service;

import com.maisprati.forum.dto.TagDto;
import com.maisprati.forum.model.Tag;
import com.maisprati.forum.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public TagDto createTag(TagDto tagDto) {
        if (tagRepository.findTagByName(tagDto.getName()).isPresent()) {
            throw new IllegalArgumentException("Já existe uma tag com o nome fornecido.");
        }
        Tag tag = new Tag();
        tag.setName(tagDto.getName());
        Tag createdTag = tagRepository.save(tag);

        return new TagDto(createdTag.getName());
    }

    public List<TagDto> getAllTags() {
        return tagRepository.findAll().stream()
                .map(tag -> new TagDto(tag.getName()))
                .collect(Collectors.toList());
    }

    public TagDto updateTag(Long id, TagDto tagDto) {
        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag não encontrada"));
        existingTag.setName(tagDto.getName());
        Tag updatedTag = tagRepository.save(existingTag);

        return new TagDto(updatedTag.getName());
    }

    public TagDto getTagByName(String name) {
        Tag tag = tagRepository.findTagByName(name)
                .orElseThrow(() -> new RuntimeException("Tag não encontrada com o nome: " + name));
        return new TagDto(tag.getName());
    }

    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }
}
