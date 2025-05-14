package com.capstone.ads.service.impl;

import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.model.Users;
import com.capstone.ads.service.AccessTokenService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;


@Service
@RequiredArgsConstructor
@Slf4j
public class AccessTokenServiceImpl implements AccessTokenService {
    @Value("${app.jwt.signature}")
    private String signature;

    @Value("${app.jwt.access-token-ttl}")
    private int accessTokenTtl;

    @Override
    public String generateAccessToken(Users user) {
        try {
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getEmail())
                    .issuer("songtaoads")
                    .issueTime(new Date())
                    .expirationTime(new Date(Instant.now().plus(accessTokenTtl, ChronoUnit.MINUTES).toEpochMilli()))
                    .claim("scope", buildScope(user))
                    .build();

            SignedJWT signedJWT = new SignedJWT(header, claimsSet);
            signedJWT.sign(new MACSigner(signature.getBytes()));
            return signedJWT.serialize();
        } catch (JOSEException e) {
            log.error("Failed to generate access token for user {}: {}", user.getEmail(), e.getMessage());
            throw new RuntimeException("Cannot generate access token", e);
        }
    }

    @Override
    public String extractEmail(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            if (signedJWT.verify(new MACVerifier(signature.getBytes()))) {
                String email = signedJWT.getJWTClaimsSet().getSubject();
                if (email == null) {
                    throw new AppException(ErrorCode.INVALID_TOKEN);
                }
                return email;
            }
            throw new AppException(ErrorCode.INVALID_TOKEN);
        } catch (Exception e) {
            log.error("Failed to extract email from token: {}", e.getMessage());
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.verify(new MACVerifier(signature.getBytes())) &&
                    signedJWT.getJWTClaimsSet().getExpirationTime().after(new Date());
        } catch (Exception e) {
            log.error("Failed to validate token: {}", e.getMessage());
            return false;
        }
    }

    private String buildScope(Users user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        var role = user.getRole().getName();
        if (!StringUtils.isEmpty(role)) {
            stringJoiner.add("ROLE_" + role);
        }
        return stringJoiner.toString();
    }
}
