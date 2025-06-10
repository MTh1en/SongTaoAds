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
    DESIGNER_NOT_FOUND("Designer not found", HttpStatus.NOT_FOUND),
    //PRODUCT TYPE
    PRODUCT_TYPE_NOT_FOUND("Product type not found", HttpStatus.NOT_FOUND),

    //PRODUCT TYPE SIZE
    PRODUCT_TYPE_SIZE_NOT_FOUND("Product type size not found", HttpStatus.NOT_FOUND),

    //SIZE
    SIZE_NOT_FOUND("Size not found", HttpStatus.NOT_FOUND),

    //ATTRIBUTE
    ATTRIBUTE_NOT_FOUND("Attribute not found", HttpStatus.NOT_FOUND),

    //ATTRIBUTE VALUE
    ATTRIBUTE_VALUE_NOT_FOUND("Attribute value not found", HttpStatus.NOT_FOUND),

    //CUSTOMER CHOICES
    CUSTOMER_CHOICES_NOT_FOUND("Customer choices not found", HttpStatus.NOT_FOUND),
    CUSTOMER_CHOICES_IS_COMPLETED("Customer choices is completed", HttpStatus.BAD_REQUEST),

    //CUSTOMER_CHOICES_SIZE
    CUSTOMER_CHOICES_SIZE_NOT_FOUND("Customer choices size not found", HttpStatus.NOT_FOUND),

    //CUSTOMER_CHOICES_DETAIL
    CUSTOMER_CHOICES_DETAIL_NOT_FOUND("Customer choices detail not found", HttpStatus.NOT_FOUND),
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
    INVALID_FORMULA("Invalid formula", HttpStatus.BAD_REQUEST),
    CALCULATION_FAILED("Calculation failed", HttpStatus.BAD_REQUEST),
    SIZE_NOT_BELONG_PRODUCT_TYPE("Size not belong to product type", HttpStatus.BAD_REQUEST),
    ATTRIBUTE_NOT_BELONG_PRODUCT_TYPE("Attribute not belong to product type", HttpStatus.BAD_REQUEST),
    UNIT_PRICE_NOT_FOUND("Unit price not enough", HttpStatus.BAD_REQUEST),
    MISSING_SIZE_VALUE("Missing value for size value", HttpStatus.BAD_REQUEST),
    INVALID_VARIABLE_VALUE("Invalid variable value for size value", HttpStatus.BAD_REQUEST),

    ATTRIBUTE_NOT_BELONG_CUSTOMER_CHOICE_DETAIL("Attribute not belong to customer choice detail", HttpStatus.BAD_REQUEST),
    ATTRIBUTE_EXISTED_IN_CUSTOMER_CHOICES_DETAIL("Attribute existed in Customer choices detail", HttpStatus.BAD_REQUEST),
    CUSTOMER_CHOICE_SIZE_EXISTED("Customer choice size already existed", HttpStatus.BAD_REQUEST),

    //FILE PROCESSING
    FILE_PROCESSING_FAILED("File processing failed", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_REQUIRED("File required", HttpStatus.BAD_REQUEST),

    DESIGN_TEMPLATE_NOT_FOUND("Design template not found", HttpStatus.NOT_FOUND),
    CUSTOM_DESIGN_REQUEST_PENDING_NOT_FOUND("Custom design request pending not found", HttpStatus.NOT_FOUND),
    CUSTOM_DESIGN_REQUEST_NOT_FOUND("Custom design request not found", HttpStatus.NOT_FOUND),

    INVALID_CUSTOM_DESIGN_REQUEST_STATUS_TRANSITION("Invalid custom design request status transition", HttpStatus.BAD_REQUEST),
    CUSTOM_DESIGN_NOT_FOUND("Custom design not found", HttpStatus.NOT_FOUND),
    INVALID_CUSTOM_DESIGN_STATUS_TRANSITION("Invalid custom design status transition", HttpStatus.BAD_REQUEST),
    CUSTOM_DESIGN_IN_WAITING_DECISION_FROM_CUSTOMER("Custom design in waiting decision from customer", HttpStatus.BAD_REQUEST),
    INVALID_ORDER_STATUS_TRANSITION("Invalid order status transition", HttpStatus.BAD_REQUEST),

    //CHAT BOT
    EXTERNAL_SERVICE_ERROR("External service error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_INPUT("Invalid input", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(String message, HttpStatusCode statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    private final String message;
    private final HttpStatusCode statusCode;
}
