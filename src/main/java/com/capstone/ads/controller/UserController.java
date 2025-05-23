package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.user.UserDTO;
import com.capstone.ads.dto.user.UserRequest;
import com.capstone.ads.service.UserService;
import com.capstone.ads.utils.ApiResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService usersService;

    @PostMapping("/users")
    public ApiResponse<UserDTO> createUser(@Valid @RequestBody UserRequest request) {
        UserDTO response = usersService.createUser(request);
        return ApiResponseBuilder.buildSuccessResponse("User created successfully", response);
    }

    @GetMapping("/users/{userId}")
    public ApiResponse<UserDTO> getUserById(@PathVariable String userId) {
        UserDTO response = usersService.getUserById(userId);
        return ApiResponseBuilder.buildSuccessResponse("User retrieved successfully", response);
    }

    @GetMapping("/users")
    public ApiResponse<List<UserDTO>> getAllUsers() {
        List<UserDTO> response = usersService.getAllUsers();
        return ApiResponseBuilder.buildSuccessResponse("Users retrieved successfully", response);
    }

    @PutMapping("/users/{userId}")
    public ApiResponse<UserDTO> updateUser(@Valid @PathVariable String userId, @RequestBody UserRequest request) {
        UserDTO response = usersService.updateUser(userId, request);
        return ApiResponseBuilder.buildSuccessResponse("User updated successfully", response);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        usersService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/users/profile")
    public ApiResponse<UserDTO> getProfile() {
        UserDTO response = usersService.getProfile();
        return ApiResponseBuilder.buildSuccessResponse("Profile retrieved successfully", response);
    }
}