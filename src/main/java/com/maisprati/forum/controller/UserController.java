package com.maisprati.forum.controller;

import com.maisprati.forum.dto.request.UserUpdateDto;
import com.maisprati.forum.dto.response.UserProfileResponseDto;
import com.maisprati.forum.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @Operation(
            summary = "Lista usuários paginados",
            description = "Retorna uma lista paginada de usuários. " +
                    "Parâmetros de consulta:\n" +
                    "- **page**: Número da página (inicia em 0). Exemplo: 0\n" +
                    "- **size**: Quantidade de registros por página. Exemplo: 10\n" +
                    "- **sort**: Critério de ordenação no formato `campo,direction`, onde `direction` pode ser `asc` ou `desc`. Exemplo: `firstName,asc`"
    )
    @GetMapping
    public ResponseEntity<Page<UserProfileResponseDto>> getAllUsers(
            @ParameterObject Pageable pageable) {
        Page<UserProfileResponseDto> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
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
