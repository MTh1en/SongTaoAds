package com.capstone.ads.service;

public interface RefreshTokenService {

    String generateRefreshToken();

    void saveRefreshToken(String email, String refreshToken);

    boolean isValid(String email, String refreshToken);

    String getEmail(String refreshToken);

    void revokeToken(String email);
}
