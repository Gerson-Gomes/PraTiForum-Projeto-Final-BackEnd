package com.maisprati.forum.controller;

import com.maisprati.forum.dto.request.UserUpdateDto;
import com.maisprati.forum.dto.response.UserProfileResponseDto;
import com.maisprati.forum.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springdoc.core.annotations.ParameterObject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @Operation(summary = "Obter todos os usuários com paginação",
            description = "Retorna uma lista paginada de perfis de usuários.",
            parameters = {
                    @Parameter(name = "page", description = "Número da página (inicia em 0)", schema = @Schema(type = "integer", defaultValue = "0")),
                    @Parameter(name = "size", description = "Quantidade de registros por página", schema = @Schema(type = "integer", defaultValue = "10"))
            })
    @GetMapping
    public ResponseEntity<Page<UserProfileResponseDto>> getAllUsers(@ParameterObject Pageable pageable) {
        Page<UserProfileResponseDto> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponseDto> getUserById(@PathVariable Long id) {
        UserProfileResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileResponseDto> updateUserProfile(
            @PathVariable Long id,
            @RequestBody UserUpdateDto userUpdateDto,
            HttpServletRequest request) {
        UserProfileResponseDto user = userService.editUser(id, userUpdateDto, request);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        userService.deleteUser(id, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserProfileResponseDto> getUserByUsername(@PathVariable String username) {
        UserProfileResponseDto user = userService.getUserByUsername(username);
        return ResponseEntity.ok().body(user);
    }
}
