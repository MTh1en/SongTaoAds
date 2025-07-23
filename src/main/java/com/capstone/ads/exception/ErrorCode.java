package com.capstone.ads.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    // ============ AUTHENTICATION & AUTHORIZATION ============
    USER_NOT_FOUND("User not found", HttpStatus.NOT_FOUND),
    INVALID_CREDENTIALS("Invalid email or password", HttpStatus.UNAUTHORIZED),
    ACCOUNT_DISABLED("Account is disabled", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED("Authentication required", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN("Invalid or expired refresh token", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED("Access denied", HttpStatus.FORBIDDEN),
    INVALID_TOKEN("Invalid token", HttpStatus.UNAUTHORIZED),
    ROLE_NOT_FOUND("Role not found", HttpStatus.NOT_FOUND),
    INVALID_PRINCIPAL("Invalid principal", HttpStatus.UNAUTHORIZED),
    ROLE_NOT_AUTHORIZED("Role not authorized", HttpStatus.UNAUTHORIZED),

    // ============ USER MANAGEMENT ============
    EMAIL_ALREADY_EXISTS("Email already exists", HttpStatus.BAD_REQUEST),
    PHONE_ALREADY_EXISTS("Phone already exists", HttpStatus.BAD_REQUEST),
    CUSTOMER_DETAIL_NOT_FOUND("Customer detail not found", HttpStatus.NOT_FOUND),
    LOGO_URL_ALREADY_EXISTS("Logo URL already exists", HttpStatus.BAD_REQUEST),

    // ============ PRODUCT RELATED ============
    PRODUCT_TYPE_NOT_FOUND("Product type not found", HttpStatus.NOT_FOUND),
    PRODUCT_TYPE_SIZE_NOT_FOUND("Product type size not found", HttpStatus.NOT_FOUND),
    SIZE_NOT_FOUND("Size not found", HttpStatus.NOT_FOUND),
    SIZE_NOT_BELONG_PRODUCT_TYPE("Size not belong to product type", HttpStatus.BAD_REQUEST),
    UNIT_PRICE_NOT_FOUND("Unit price not enough", HttpStatus.BAD_REQUEST),
    SIZE_VALUE_OUT_OF_RANGE("Size value out of range", HttpStatus.BAD_REQUEST),

    // ============ ATTRIBUTE RELATED ============
    ATTRIBUTE_NOT_FOUND("Attribute not found", HttpStatus.NOT_FOUND),
    ATTRIBUTE_VALUE_NOT_FOUND("Attribute value not found", HttpStatus.NOT_FOUND),
    ATTRIBUTE_NOT_BELONG_PRODUCT_TYPE("Attribute not belong to product type", HttpStatus.BAD_REQUEST),
    ATTRIBUTE_NOT_BELONG_CUSTOMER_CHOICE_DETAIL("Attribute not belong to customer choice detail", HttpStatus.BAD_REQUEST),
    ATTRIBUTE_EXISTED_IN_CUSTOMER_CHOICES_DETAIL("Attribute existed in Customer choices detail", HttpStatus.BAD_REQUEST),

    // ============ CUSTOMER CHOICES ============
    CUSTOMER_CHOICES_NOT_FOUND("Customer choices not found", HttpStatus.NOT_FOUND),
    CUSTOMER_CHOICES_IS_COMPLETED("Customer choices is completed", HttpStatus.BAD_REQUEST),
    CUSTOMER_CHOICES_SIZE_NOT_FOUND("Customer choices size not found", HttpStatus.NOT_FOUND),
    CUSTOMER_CHOICES_DETAIL_NOT_FOUND("Customer choices detail not found", HttpStatus.NOT_FOUND),
    CUSTOMER_CHOICE_SIZE_EXISTED("Customer choice size already existed", HttpStatus.BAD_REQUEST),

    // ============ DESIGN RELATED ============
    DESIGNER_NOT_FOUND("Designer not found", HttpStatus.NOT_FOUND),
    AI_DESIGN_NOT_FOUND("AI Design not found", HttpStatus.NOT_FOUND),
    AI_DESIGN_ALREADY_USED("AI Design is already associated with another order", HttpStatus.BAD_REQUEST),
    DESIGN_TEMPLATE_NOT_FOUND("Design template not found", HttpStatus.NOT_FOUND),
    CUSTOM_DESIGN_REQUEST_DEPOSITED_NOT_FOUND("Custom design request deposited not found", HttpStatus.NOT_FOUND),
    CUSTOM_DESIGN_REQUEST_NOT_FOUND("Custom design request not found", HttpStatus.NOT_FOUND),
    CUSTOM_DESIGN_NOT_FOUND("Custom design not found", HttpStatus.NOT_FOUND),
    INVALID_CUSTOM_DESIGN_REQUEST_STATUS_TRANSITION("Invalid custom design request status transition", HttpStatus.BAD_REQUEST),
    INVALID_CUSTOM_DESIGN_STATUS_TRANSITION("Invalid custom design status transition", HttpStatus.BAD_REQUEST),
    DEMO_DESIGN_NOT_FOUND("Demo design not found", HttpStatus.NOT_FOUND),
    DEMO_DESIGN_IN_WAITING_DECISION_FROM_CUSTOMER("Demo design in waiting decision from customer", HttpStatus.BAD_REQUEST),
    BACKGROUND_NOT_FOUND("Background not found", HttpStatus.NOT_FOUND),
    ICON_NOT_FOUND("Icon not found", HttpStatus.NOT_FOUND),

    // ============ ORDER & PAYMENT ============
    ORDER_NOT_FOUND("Order not found", HttpStatus.NOT_FOUND),
    ORDER_DETAIL_NOT_FOUND("Order detail not found", HttpStatus.NOT_FOUND),
    ORDER_DETAIL_ALREADY_EXISTS("Order detail already exists", HttpStatus.BAD_REQUEST),
    CONFLICTING_DESIGN_SOURCES("Order detail cannot be created from both a custom design request and a customer choice simultaneously.", HttpStatus.BAD_REQUEST),
    INVALID_ORDER_STATUS("Invalid order status", HttpStatus.BAD_REQUEST),
    INVALID_ORDER_STATUS_TRANSITION("Invalid order status transition", HttpStatus.BAD_REQUEST),
    PAYMENT_NOT_FOUND("Payment not found", HttpStatus.NOT_FOUND),
    PAYMENT_FAILED("Failed to initiate payment", HttpStatus.INTERNAL_SERVER_ERROR),
    PAYMENT_STATUS_CHECK_FAILED("Failed to check payment status", HttpStatus.INTERNAL_SERVER_ERROR),
    SIGNATURE_GENERATION_FAILED("Failed to generate signature", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_SIGNATURE("Invalid callback signature", HttpStatus.BAD_REQUEST),
    INVALID_STATUS_PAYMENT("Invalid status payment", HttpStatus.BAD_REQUEST),

    // ============ CALCULATION & FORMULA ============
    INVALID_FORMULA("Invalid formula", HttpStatus.BAD_REQUEST),
    CALCULATION_FAILED("Calculation failed", HttpStatus.BAD_REQUEST),
    MISSING_SIZE_VALUE("Missing value for size value", HttpStatus.BAD_REQUEST),
    INVALID_VARIABLE_VALUE("Invalid variable value for size value", HttpStatus.BAD_REQUEST),

    // ============ FILE PROCESSING ============
    FILE_PROCESSING_FAILED("File processing failed", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_REQUIRED("File required", HttpStatus.BAD_REQUEST),
    FILE_NOT_FOUND("File not found", HttpStatus.NOT_FOUND),

    // ============ TICKET SYSTEM ============
    TICKET_NOT_FOUND("Ticket not found", HttpStatus.NOT_FOUND),
    TICKET_NOT_OPEN("Ticket not open", HttpStatus.BAD_REQUEST),
    PROVIE_SOLUTION_TICKET("Provie solution ticket", HttpStatus.BAD_REQUEST),
    INVALID_SEVERITY("Invalid severity", HttpStatus.BAD_REQUEST),

    // ============ BUSINESS ENTITIES ============
    PRICE_PROPOSAL_NOT_FOUND("Price proposal not found", HttpStatus.NOT_FOUND),
    CONTRACT_NOT_FOUND("Contract not found", HttpStatus.NOT_FOUND),
    FEEDBACK_NOT_FOUND("Feedback not found", HttpStatus.NOT_FOUND),
    COST_TYPE_NOT_FOUND("Cost type not found", HttpStatus.NOT_FOUND),

    // ============ EXTERNAL SERVICES ============
    EXTERNAL_SERVICE_ERROR("External service error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_INPUT("Invalid input", HttpStatus.BAD_REQUEST),

    // ============ AI ============
    MODEL_CHAT_NOT_FOUND("Model chat not fount", HttpStatus.NOT_FOUND),
    ;

    ErrorCode(String message, HttpStatusCode statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    private final String message;
    private final HttpStatusCode statusCode;
}
