package com.capstone.ads.service;

import org.springframework.security.access.prepost.PreAuthorize;

public interface RefreshTokenService {

    String generateRefreshToken();

    void saveRefreshToken(String email, String refreshToken);

    boolean isValid(String email, String refreshToken);

    String getEmail(String refreshToken);

    void revokeToken(String email);
}
