package com.maisprati.forum.controller;


import com.maisprati.forum.dto.request.UserUpdateDto;
import com.maisprati.forum.dto.response.UserProfileResponseDto;
import com.maisprati.forum.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<UserProfileResponseDto>> getAllUsers() {
        var users = userService.getAllUsers();
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponseDto> getUserById(@PathVariable Long id) {
        var user = userService.getUserById(id);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileResponseDto> updateUserProfile(
            @PathVariable Long id,
            @RequestBody UserUpdateDto userUpdateDto,
            HttpServletRequest request) {
        var user = userService.editUser(id, userUpdateDto, request);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        userService.deleteUser(id, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserProfileResponseDto> getUserByUsername(@PathVariable String username) {
        var user = userService.getUserByUsername(username);
        return ResponseEntity.ok().body(user);
    }
}