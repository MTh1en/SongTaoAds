package com.capstone.ads.utils;

import com.capstone.ads.dto.ApiResponse;
import lombok.experimental.UtilityClass;

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
}
