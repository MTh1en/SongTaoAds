package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.auth.AuthResponse;
import com.capstone.ads.dto.auth.LoginRequest;
import com.capstone.ads.dto.auth.RegisterRequest;
import com.capstone.ads.service.AuthService;
import com.capstone.ads.service.RecoveryService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "AUTH")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;
    RecoveryService recoveryService;

    @PostMapping("/outbound/authentication")
    public ApiResponse<AuthResponse> outboundAuthentication(@RequestParam String code, HttpServletResponse response) {
        var result = authService.outboundAuthenticate(code, response);
        return ApiResponseBuilder.buildSuccessResponse("Login successful", result);
    }

    @PostMapping("/login")
    @Operation(summary = "Đăng nhập")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        var result = authService.login(request, response);
        return ApiResponseBuilder.buildSuccessResponse("Login successful", result);
    }

    @PostMapping("/register")
    @Operation(summary = "Đăng ký")
    public ApiResponse<Void> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        recoveryService.sendVerifyEmail(request.getEmail());
        return ApiResponseBuilder.buildSuccessResponse("Registration successful", null);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Làm mới token")
    public ApiResponse<AuthResponse> refreshToken(@CookieValue(value = "refresh_token", required = false) String refreshToken,
                                                  HttpServletResponse response) {
        var result = authService.refreshToken(refreshToken, response);
        return ApiResponseBuilder.buildSuccessResponse("Refresh token successful", result);
    }

    @PostMapping("/logout")
    @Operation(summary = "Đăng xuất")
    public ApiResponse<Void> logout(@CookieValue(value = "refresh_token", required = false) String refreshToken,
                                    HttpServletResponse response) {
        authService.logout(refreshToken, response);
        return ApiResponseBuilder.buildSuccessResponse("Logout successful", null);
    }
}
