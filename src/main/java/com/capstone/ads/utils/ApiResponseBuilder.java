package com.capstone.ads.utils;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Utility class to build standardized API responses.
 */
@UtilityClass
public class ApiResponseBuilder {

    /**
     * Builds a success API response with a message and optional result.
     *
     * @param message The success message.
     * @param result The result data (can be null).
     * @param <T> The type of the result data.
     * @return A built ApiResponse instance.
     */
    public static <T> ApiResponse<T> buildSuccessResponse(String message, T result) {
        return ApiResponse.<T>builder()
                .message(message)
                .result(result)
                .build();
    }

    /**
     * Builds a success paging API response from a Page object.
     *
     * @param message The success message.
     * @param page The Page object containing data and metadata.
     * @param result The list of result data (mapped DTOs).
     * @param pageNumber The current page number (1-based).
     * @param <T> The type of the result data.
     * @return A built ApiPagingResponse instance.
     */
    public static <T> ApiPagingResponse<T> buildPagingSuccessResponse(String message, Page<T> page, int pageNumber) {
        return ApiPagingResponse.<T>builder()
                .success(true)
                .message(message)
                .result(page.getContent())
                .currentPage(pageNumber)
                .totalPages(page.getTotalPages())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .build();
    }

    /**
     * Builds an error API response with a message.
     *
     * @param message The error message.
     * @param <T> The type of the result data.
     * @return A built ApiResponse instance.
     */
    public static <T> ApiResponse<T> buildErrorResponse(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .result(null)
                .build();
    }

    /**
     * Builds an error paging API response with a message.
     *
     * @param message The error message.
     * @param <T> The type of the result data.
     * @return A built ApiPagingResponse instance.
     */
    public static <T> ApiPagingResponse<T> buildPagingErrorResponse(String message) {
        return ApiPagingResponse.<T>builder()
                .success(false)
                .message(message)
                .result(null)
                .currentPage(0)
                .totalPages(0)
                .pageSize(0)
                .totalElements(0L)
                .build();
    }
}
