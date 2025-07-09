package com.capstone.ads.filter;

import com.capstone.ads.service.AccessTokenService;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomJwtDecoder implements JwtDecoder {
    private final AccessTokenService accessTokenService;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            if (token == null || token.isEmpty()) {
                throw new JwtException("Token is empty or null");
            }

            SignedJWT signedJWT = SignedJWT.parse(token);

            // Validate expiration
            if (!accessTokenService.isTokenValid(token)) {
                throw new JwtException("Token has expired");
            }

            return new Jwt(
                    token,
                    signedJWT.getJWTClaimsSet().getIssueTime().toInstant(),
                    signedJWT.getJWTClaimsSet().getExpirationTime().toInstant(),
                    signedJWT.getHeader().toJSONObject(),
                    signedJWT.getJWTClaimsSet().toJSONObject()
            );
        } catch (ParseException e) {
            log.error("Invalid token: {}", e.getMessage());
            throw new JwtException("Invalid token: " + e.getMessage());
        }
    }
}
