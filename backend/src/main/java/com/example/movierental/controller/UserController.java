package com.example.movierental.controller;

import com.example.movierental.entity.User;
import com.example.movierental.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) { this.userService = userService; }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }
    @GetMapping
    public ResponseEntity<List<User>> getAll() { return ResponseEntity.ok(userService.getAllUsers()); }
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) { return ResponseEntity.ok(userService.getUserById(id)); }
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { userService.deleteUser(id); return ResponseEntity.noContent().build(); }
}
