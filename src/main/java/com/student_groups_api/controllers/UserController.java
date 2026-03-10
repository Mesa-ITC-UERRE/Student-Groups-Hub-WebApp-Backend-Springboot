package com.student_groups_api.controllers;

import com.student_groups_api.dto.users.UserResponse;
import com.student_groups_api.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() {
        return ResponseEntity.ok(userService.me());
    }

    @GetMapping("/by-entra/{entraOid}")
    public ResponseEntity<UserResponse> getByEntraOid(@PathVariable String entraOid) {
        return ResponseEntity.ok(userService.getByEntraOid(entraOid));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getById(id));
    }
}