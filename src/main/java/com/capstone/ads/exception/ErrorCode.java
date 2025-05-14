package com.capstone.ads.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND("User not found", HttpStatus.NOT_FOUND),
    INVALID_CREDENTIALS("Invalid email or password", HttpStatus.UNAUTHORIZED),
    ACCOUNT_DISABLED("Account is disabled", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED("Authentication required", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN("Invalid or expired refresh token", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED("Access denied", HttpStatus.FORBIDDEN),
    ROLE_NOT_FOUND("Role not found", HttpStatus.NOT_FOUND),
    INVALID_TOKEN("Invalid token", HttpStatus.UNAUTHORIZED),
    ;

    ErrorCode(String message, HttpStatusCode statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    private final String message;
    private final HttpStatusCode statusCode;
}
