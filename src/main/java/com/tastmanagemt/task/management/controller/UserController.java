package com.tastmanagemt.task.management.controller;

import com.tastmanagemt.task.management.dto.ApiResponse;
import com.tastmanagemt.task.management.model.User;
import com.tastmanagemt.task.management.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<User>> saveUser(@Valid @RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<User>> getUsersPaginated(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        return userService.getUsersPaginated(page, size);
    }

    @GetMapping("/{useruid}")
    public ResponseEntity<User> getUserById(@PathVariable Long useruid) {
        return userService.getUserById(useruid);
    }

    @PutMapping("/{useruid}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Long useruid, @Valid @RequestBody User user) {
        return userService.updateUser(useruid, user);
    }

    @DeleteMapping("/{useruid}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long useruid) {
        return userService.deleteUser(useruid);
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String firstName) {
        return userService.searchUsersByFirstName(firstName);
    }

    @PutMapping("/{useruid}/deactivate")
    public ResponseEntity<ApiResponse<User>> deactivateUser(@PathVariable Long useruid) {
        return userService.deactivate(useruid);
    }

    @PutMapping("/{useruid}/activate")
    public ResponseEntity<ApiResponse<User>> activateUser(@PathVariable Long useruid) {
        return userService.activate(useruid);
    }

    @GetMapping("/active")
    public ResponseEntity<List<User>> getActiveUsers() {
        return userService.getActiveUsers();
    }
}
