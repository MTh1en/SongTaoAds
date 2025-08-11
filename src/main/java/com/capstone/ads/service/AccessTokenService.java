package com.capstone.ads.service;

import com.capstone.ads.model.Users;

public interface AccessTokenService {
    String generateAccessToken(Users users);

    String extractUserId(String token);

    String extractRole(String token);

    boolean isTokenValid(String token);
}
