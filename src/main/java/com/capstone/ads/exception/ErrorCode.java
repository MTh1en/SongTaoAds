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
    //AI_DESIGN
    AI_DESIGN_NOT_FOUND("AI Design not found", HttpStatus.NOT_FOUND),
    AI_DESIGN_ALREADY_USED("AI Design is already associated with another order", HttpStatus.BAD_REQUEST),

    //ORDER
    ORDER_NOT_FOUND("Order not found", HttpStatus.NOT_FOUND),
    INVALID_ORDER_STATUS("Invalid order status", HttpStatus.BAD_REQUEST),
    //Payment

    PAYMENT_NOT_FOUND("Payment not found", HttpStatus.NOT_FOUND),
    PAYMENT_FAILED("Failed to initiate payment", HttpStatus.INTERNAL_SERVER_ERROR),
    PAYMENT_STATUS_CHECK_FAILED("Failed to check payment status", HttpStatus.INTERNAL_SERVER_ERROR),
    SIGNATURE_GENERATION_FAILED("Failed to generate signature", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_SIGNATURE("Invalid callback signature", HttpStatus.BAD_REQUEST),
    INVALID_STATUS_PAYMENT("Invalid status payment", HttpStatus.BAD_REQUEST),
    //USER
    EMAIL_ALREADY_EXISTS("Email already exists", HttpStatus.BAD_REQUEST),
    PHONE_ALREADY_EXISTS("Phone already exists", HttpStatus.BAD_REQUEST),
    //CUSTOMER_DETAIL
    LOGO_URL_ALREADY_EXISTS("Logo URL already exists", HttpStatus.BAD_REQUEST),
    CUSTOMER_DETAIL_NOT_FOUND("Customer detail not found", HttpStatus.NOT_FOUND),
    ;

    ErrorCode(String message, HttpStatusCode statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    private final String message;
    private final HttpStatusCode statusCode;
}
