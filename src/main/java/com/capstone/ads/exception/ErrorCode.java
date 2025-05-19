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
    INVALID_PRINCIPAL("Invalid principal", HttpStatus.UNAUTHORIZED),
    //PRODUCT TYPE
    PRODUCT_TYPE_NOT_FOUND("Product type not found", HttpStatus.NOT_FOUND),

    //SIZE
    SIZE_NOT_FOUND("Size not found", HttpStatus.NOT_FOUND),

    //ATTRIBUTE
    ATTRIBUTE_NOT_FOUND("Attribute not found", HttpStatus.NOT_FOUND),

    //ATTRIBUTE VALUE
    ATTRIBUTE_VALUE_NOT_FOUND("Attribute value not found", HttpStatus.NOT_FOUND),

    //CUSTOMER CHOICES
    CUSTOMER_CHOICES_NOT_FOUND("Customer choices not found", HttpStatus.NOT_FOUND),
    CUSTOMER_CHOICES_IS_COMPLETED("Customer choices is completed", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(String message, HttpStatusCode statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    private final String message;
    private final HttpStatusCode statusCode;
}
