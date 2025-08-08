package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.user.*;
import com.capstone.ads.service.UserService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "USER")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService usersService;

    @PostMapping("/users")
    @Operation(summary = "Tạo tài khoản hệ thống")
    public ApiResponse<UserDTO> createUser(@Valid @RequestBody UserCreateRequest request) {
        var response = usersService.createUser(request);
        return ApiResponseBuilder.buildSuccessResponse("Tạo tài khoản hệ thống thành công", response);
    }

    @GetMapping("/users/{userId}")
    @Operation(summary = "Xem thông tin người dùng theo ID")
    public ApiResponse<UserDTO> getUserById(@PathVariable String userId) {
        var response = usersService.findUserById(userId);
        return ApiResponseBuilder.buildSuccessResponse("Xem tất cả thông tin người dùng theo ID thành công", response);
    }

    @GetMapping("/users/role")
    @Operation(summary = "Xem thông tin người dùng theo role")
    public ApiPagingResponse<UserDTO> getUsersByRole(
            @RequestParam(value = "roleName", defaultValue = "CUSTOMER") String roleName,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = usersService.findUsersByRoleName(roleName, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Xem thông tin người dùng theo role thành công", response, page);
    }

    @GetMapping("/users")
    @Operation(summary = "Xem tất cả thông tin người dùng")
    public ApiPagingResponse<UserDTO> getAllUsers(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = usersService.findAllUsers(page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Xem tất cả thông tin người dùng thành công", response, page);
    }

    @PatchMapping("/users/{userId}/profile")
    @Operation(summary = "Xem profile của tài khoản theo ID")
    public ApiResponse<UserDTO> updateUserProfile(@PathVariable String userId,
                                                  @Valid @RequestBody UserProfileUpdateRequest request) {
        var response = usersService.updateUserProfile(userId, request);
        return ApiResponseBuilder.buildSuccessResponse("Xem profile của tài khoản theo ID thành công", response);
    }

    @PatchMapping(value = "/users/{userId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Cập nhật avatar")
    public ApiResponse<UserDTO> updateUserAvatar(@PathVariable String userId,
                                                 @RequestPart("avatar") MultipartFile avatar) throws IOException {
        var response = usersService.uploadUserAvatar(userId, avatar);
        return ApiResponseBuilder.buildSuccessResponse("Cập nhật avatar thành công", response);
    }

    @PatchMapping("/users/{userId}/password")
    @Operation(summary = "Cập nhật mật khẩu")
    public ApiResponse<UserDTO> updateUserPassword(@PathVariable String userId,
                                                   @Valid @RequestBody ChangePasswordRequest request) {
        var response = usersService.changePassword(userId, request);
        return ApiResponseBuilder.buildSuccessResponse("Cập nhật mật khẩu thành công", response);
    }

    @PatchMapping("/users/{userId}/new-password")
    @Operation(summary = "Tạo mới mật khẩu cho tài khoản ngoài hệ thống")
    public ApiResponse<UserDTO> newPasswordForOutboundUser(@PathVariable String userId,
                                                           @Valid @RequestBody NewPasswordRequest request) {
        var response = usersService.newPasswordForOutboundAccount(userId, request);
        return ApiResponseBuilder.buildSuccessResponse("Tạo mới mật khẩu cho tài khoản ngoài hệ thống thành công", response);
    }

    @DeleteMapping("/users/{userId}")
    @Operation(summary = "Xóa cứng tài khoản")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        usersService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/profile")
    @Operation(summary = "Xem profile tài khoản đang đang nhập hiện tại")
    public ApiResponse<UserDTO> getProfile() {
        var response = usersService.getCurrentUserProfile();
        return ApiResponseBuilder.buildSuccessResponse("Xem profile tài khoản đang đang nhập hiện tại thành công", response);
    }

}