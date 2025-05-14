package com.capstone.ads.service;

import com.capstone.ads.model.Users;

public interface AccessTokenService {
    String generateAccessToken(Users users);

    String extractEmail(String token);

    boolean isTokenValid(String token);
}
