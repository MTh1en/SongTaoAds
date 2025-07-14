package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.RedisKeyNaming;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.service.RefreshTokenService;
import io.micrometer.common.util.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @NonFinal
    @Value("${app.jwt.refresh-token-ttl}")
    private int refreshTokenTtl;

    private final RedisTemplate<String, String> redisTemplate;


    @Override
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void saveRefreshToken(String email, String refreshToken) {
        String tokenHashed = hashToken(refreshToken);
        String tokenKey = RedisKeyNaming.REFRESH_TOKEN + tokenHashed;
        String userTokenSetKey = RedisKeyNaming.USER_REFRESH_TOKENS + email;

        String tokenStorageInRedis = BCrypt.hashpw(refreshToken, BCrypt.gensalt());
        Instant now = Instant.now();

        redisTemplate.opsForHash().put(tokenKey, "email", email);
        redisTemplate.opsForHash().put(tokenKey, "created", now.toString());
        redisTemplate.opsForHash().put(tokenKey, "expires", now.plus(refreshTokenTtl, ChronoUnit.MINUTES).toString());
        redisTemplate.opsForHash().put(tokenKey, "hashedToken", tokenStorageInRedis);

        redisTemplate.opsForSet().add(userTokenSetKey, tokenHashed);

        redisTemplate.expire(tokenKey, refreshTokenTtl, TimeUnit.MINUTES);
        redisTemplate.expire(userTokenSetKey, refreshTokenTtl, TimeUnit.MINUTES);
    }

    @Override
    public boolean isValid(String email, String refreshToken) {
        String tokenHashed = hashToken(refreshToken);
        String tokenKey = RedisKeyNaming.REFRESH_TOKEN + tokenHashed;
        String userTokenSetKey = RedisKeyNaming.USER_REFRESH_TOKENS + email;

        if (!isTokenInUserSet(userTokenSetKey, tokenHashed)) {
            log.warn("Refresh token not found in user's token set for email: {}", email);
            return false;
        }

        String expires = (String) redisTemplate.opsForHash().get(tokenKey, "expires");
        String storedHashedToken = (String) redisTemplate.opsForHash().get(tokenKey, "hashedToken");

        if (StringUtils.isEmpty(expires) || StringUtils.isEmpty(storedHashedToken)) {
            log.warn("Invalid or missing refresh token data for email: {}", email);
            return false;
        }

        if (isTokenExpired(expires)) {
            log.warn("Refresh token expired for email: {}", email);
            return false;
        }

        return BCrypt.checkpw(refreshToken, storedHashedToken);
    }

    @Override
    public String getEmail(String refreshToken) {
        String tokenHashed = hashToken(refreshToken);
        String tokenKey = RedisKeyNaming.REFRESH_TOKEN + tokenHashed;

        String email = (String) redisTemplate.opsForHash().get(tokenKey, "email");
        if (StringUtils.isEmpty(email)) {
            log.warn("No email found for refresh token");
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }
        return email;
    }

    @Override
    public void revokeToken(String email) {
        String userTokenSetKey = RedisKeyNaming.USER_REFRESH_TOKENS + email;
        Set<String> tokenHashes = redisTemplate.opsForSet().members(userTokenSetKey);
        if (tokenHashes.isEmpty()) {
            log.info("No refresh tokens to revoke for email: {}", email);
            return;
        }
        tokenHashes.forEach(tokenHash -> {
            String tokenKey = RedisKeyNaming.REFRESH_TOKEN + tokenHash;
            redisTemplate.delete(tokenKey);
        });
        redisTemplate.delete(userTokenSetKey);
        log.info("Revoked all refresh tokens for email: {}", email);
    }

    public String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash token", e);
        }
    }

    private boolean isTokenInUserSet(String userTokenSetKey, String tokenHashed) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(userTokenSetKey, tokenHashed));
    }

    private boolean isTokenExpired(String expires) {
        return Instant.parse(expires).isBefore(Instant.now());
    }
}
