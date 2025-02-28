package com.maisprati.forum.controller;

import com.maisprati.forum.dto.request.ForumDto;
import com.maisprati.forum.service.ForumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForumController {

    @Autowired
    private ForumService forumService;

    @GetMapping("/forum")
    public ForumDto getForum() {
        return forumService.getForumDetails();
    }

    @Operation(summary = "Obter fóruns com paginação",
            description = "Retorna uma lista paginada de fóruns.",
            parameters = {
                    @Parameter(name = "page", description = "Número da página (inicia em 0)", schema = @Schema(type = "integer", defaultValue = "0")),
                    @Parameter(name = "size", description = "Quantidade de registros por página", schema = @Schema(type = "integer", defaultValue = "10"))
            })
    @GetMapping("/forums")
    public Page<ForumDto> getAllForums(@ParameterObject Pageable pageable) {
        return forumService.getAllForums(pageable);
    }
}
