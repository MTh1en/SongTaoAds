package com.capstone.ads.filter;

import com.capstone.ads.service.AccessTokenService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomJwtDecoder implements JwtDecoder {

    @Value("${app.jwt.signature}")
    private String signature;

    private final AccessTokenService accessTokenService;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            // Validate signature
            if (!signedJWT.verify(new MACVerifier(signature.getBytes()))) {
                throw new JwtException("Invalid token signature");
            }

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
        } catch (ParseException | JOSEException e) {
            log.error("Invalid token: {}", e.getMessage());
            throw new JwtException("Invalid token: " + e.getMessage());
        }
    }
}
