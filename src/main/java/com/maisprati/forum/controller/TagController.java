package com.maisprati.forum.controller;

import com.maisprati.forum.dto.TagDto;
import com.maisprati.forum.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

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

    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Lista tópicos paginados",
            description = "Retorna uma lista paginada de tópicos.\n\n" +
                    "Parâmetros de consulta:\n" +
                    "- **page**: Número da página (inicia em 0). Exemplo: 0\n" +
                    "- **size**: Quantidade de registros por página. Exemplo: 10\n" +
                    "- **sort**: Critério de ordenação no formato `campo,direction`, onde `direction` pode ser `asc` ou `desc`. Exemplo: `title,asc`"
    )
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
