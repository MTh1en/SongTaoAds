package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.user.UserDTO;
import com.capstone.ads.dto.user.UserRequestDTO;
import com.capstone.ads.service.UserService;
import com.capstone.ads.service.UserService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService usersService;

    @PostMapping
    public ApiResponse<UserDTO> createUser(@RequestBody UserRequestDTO request) {
        UserDTO response = usersService.createUser(request);
        return ApiResponseBuilder.buildSuccessResponse("User created successfully", response);
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDTO> getUserById(@PathVariable String id) {
        UserDTO response = usersService.getUserById(id);
        return ApiResponseBuilder.buildSuccessResponse("User retrieved successfully", response);
    }

    @GetMapping
    public ApiResponse<List<UserDTO>> getAllUsers() {
        List<UserDTO> response = usersService.getAllUsers();
        return ApiResponseBuilder.buildSuccessResponse("Users retrieved successfully", response);
    }

    @PutMapping("/{id}")
    public ApiResponse<UserDTO> updateUser(@PathVariable String id, @RequestBody UserRequestDTO request) {
        UserDTO response = usersService.updateUser(id, request);
        return ApiResponseBuilder.buildSuccessResponse("User updated successfully", response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        usersService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/profile")
    public ApiResponse<UserDTO> getProfile() {
        UserDTO response = usersService.getProfile();
        return ApiResponseBuilder.buildSuccessResponse("Profile retrieved successfully", response);
    }
}