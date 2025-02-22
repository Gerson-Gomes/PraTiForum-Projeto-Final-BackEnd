package com.maisprati.forum.controller;

import com.maisprati.forum.dto.TagDto;
import com.maisprati.forum.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TagDto> createTag(@RequestBody TagDto tagDto) {
        TagDto createdTagDto = tagService.createTag(tagDto);
        return ResponseEntity.ok(createdTagDto);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TagDto> getTagByName(@RequestParam String name) {
        TagDto tagDto = tagService.getTagByName(name);
        return ResponseEntity.ok(tagDto);
    }

    @Operation(summary = "Obter todas as tags com paginação",
            description = "Retorna uma lista paginada de tags.",
            parameters = {
                    @Parameter(name = "page", description = "Número da página (inicia em 0)", schema = @Schema(type = "integer", defaultValue = "0")),
                    @Parameter(name = "size", description = "Quantidade de registros por página", schema = @Schema(type = "integer", defaultValue = "10"))
            })
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<Page<TagDto>> getAllTags(@ParameterObject Pageable pageable) {
        Page<TagDto> tagDtos = tagService.getAllTags(pageable);
        return ResponseEntity.ok(tagDtos);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TagDto> updateTag(@PathVariable Long id, @RequestBody TagDto tagDto) {
        TagDto updatedTagDto = tagService.updateTag(id, tagDto);
        return ResponseEntity.ok(updatedTagDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.ok("Tag deletada com sucesso!");
    }
}
