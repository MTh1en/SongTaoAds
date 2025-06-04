package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.user.ChangePasswordRequest;
import com.capstone.ads.dto.user.UserDTO;
import com.capstone.ads.dto.user.UserCreateRequest;
import com.capstone.ads.dto.user.UserProfileUpdateRequest;
import com.capstone.ads.service.UserService;
import com.capstone.ads.utils.ApiResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService usersService;

    @PostMapping("/users")
    public ApiResponse<UserDTO> createUser(@Valid @RequestBody UserCreateRequest request) {
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

    @PatchMapping("/users/{userId}/profile")
    public ApiResponse<UserDTO> updateUserProfile(@Valid @PathVariable String userId, @RequestBody UserProfileUpdateRequest request) {
        UserDTO response = usersService.updateUserProfile(userId, request);
        return ApiResponseBuilder.buildSuccessResponse("User updated successfully", response);
    }

    @PatchMapping(value = "/users/{userId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<UserDTO> updateUserAvatar(@PathVariable String userId,
                                                 @RequestPart("avatar") MultipartFile avatar) throws IOException {
        var response = usersService.uploadUserAvatar(userId, avatar);
        return ApiResponseBuilder.buildSuccessResponse("User updated successfully", response);
    }

    @PatchMapping("/users/{userId}/password")
    public ApiResponse<UserDTO> updateUserPassword(@PathVariable String userId,
                                                   @RequestBody ChangePasswordRequest request) {
        var response = usersService.changePassword(userId, request);
        return ApiResponseBuilder.buildSuccessResponse("User updated successfully", response);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        usersService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/profile")
    public ApiResponse<UserDTO> getProfile() {
        UserDTO response = usersService.getCurrentUserProfile();
        return ApiResponseBuilder.buildSuccessResponse("Profile retrieved successfully", response);
    }

}