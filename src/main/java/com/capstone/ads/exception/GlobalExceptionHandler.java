package com.capstone.ads.exception;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAllExceptions(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseBuilder.buildErrorResponse("Unknown Exception", ex.getMessage()));
    }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse<Object>> handleAppException(AppException ex) {
        return ResponseEntity
                .status(ex.getErrorCode().getStatusCode())
                .body(ApiResponseBuilder.buildErrorResponse(ex.getMessage(), null));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseBuilder.buildErrorResponse("Validation Error", errors));
    }

    @ExceptionHandler(value = HandlerMethodValidationException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodValidationException(HandlerMethodValidationException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getValueResults().forEach(error -> errors.put(
                error.getMethodParameter().getParameterName(),
                error.getResolvableErrors().getFirst().getDefaultMessage()));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseBuilder.buildErrorResponse("Validation Error", errors));
    }

    @ExceptionHandler(value = AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(AuthorizationDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponseBuilder.buildErrorResponse(ex.getMessage(), null));
    }

    @ExceptionHandler(value = DataAccessException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataAccessException(PSQLException ex) {
        return switch (ex.getSQLState()) {
            case "23505" -> ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiResponseBuilder.buildErrorResponse("Thông tin đã tồn tại", ex.getMessage()));
            case "23503" -> ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiResponseBuilder.buildErrorResponse("Thông tin đã được sử dụng ở phần khác", ex.getMessage()));
            default -> ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiResponseBuilder.buildErrorResponse("Database Error", ex.getServerErrorMessage()));
        };

    }
}
