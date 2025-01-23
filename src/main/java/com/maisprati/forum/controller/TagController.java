package com.maisprati.forum.controller;

import com.maisprati.forum.dto.TagDto;
import com.maisprati.forum.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @PostMapping
    public ResponseEntity<TagDto> createTag(@RequestBody TagDto tagDto) {
        TagDto createdTagDto = tagService.createTag(tagDto);
        return ResponseEntity.ok(createdTagDto);
    }

    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTags() {
        List<TagDto> tagDtos = tagService.getAllTags();
        return ResponseEntity.ok(tagDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagDto> updateTag(@PathVariable Long id, @RequestBody TagDto tagDto) {
        TagDto updatedTagDto = tagService.updateTag(id, tagDto);
        return ResponseEntity.ok(updatedTagDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.ok("Tag deletada com sucesso!");
    }
}
