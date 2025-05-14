package com.capstone.ads.service;

import com.capstone.ads.dto.auth.AuthResponse;
import com.capstone.ads.dto.auth.LoginRequest;
import com.capstone.ads.dto.auth.RegisterRequest;
import jakarta.servlet.http.HttpServletResponse;


public interface AuthService {
    AuthResponse login(LoginRequest request, HttpServletResponse response);

    void register(RegisterRequest request);

    AuthResponse refreshToken(String refreshToken, HttpServletResponse response);

    void logout(String refreshToken, HttpServletResponse response);
}
