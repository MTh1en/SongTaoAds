package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.auth.AuthResponse;
import com.capstone.ads.dto.auth.LoginRequest;
import com.capstone.ads.dto.auth.RegisterRequest;
import com.capstone.ads.service.AuthService;
import com.capstone.ads.utils.ApiResponseBuilder;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    AuthService authService;

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        var result = authService.login(request, response);
        return ApiResponseBuilder.buildSuccessResponse("Login successful", result);
    }

    @PostMapping("/register")
    public ApiResponse<Void> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ApiResponseBuilder.buildSuccessResponse("Registration successful", null);
    }

    @PostMapping("/refresh-token")
    public ApiResponse<AuthResponse> refreshToken(@CookieValue(value = "refresh_token", required = false) String refreshToken,
                                                  HttpServletResponse response) {
        var result = authService.refreshToken(refreshToken, response);
        return ApiResponseBuilder.buildSuccessResponse("Refresh token successful", result);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@CookieValue(value = "refresh_token", required = false) String refreshToken,
                                    HttpServletResponse response) {
        authService.logout(refreshToken, response);
        return ApiResponseBuilder.buildSuccessResponse("Logout successful", null);
    }
}
