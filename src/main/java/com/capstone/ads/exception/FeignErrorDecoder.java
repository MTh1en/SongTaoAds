package com.capstone.ads.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;

import javax.naming.ServiceUnavailableException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        String requestUrl = response.request().url();
        log.error("Error decoding Feign response for method: {} at URL: {}", methodKey, requestUrl);
        log.error("HTTP Status: {}", response.status());

        String responseBody = null;
        try {
            if (response.body() != null) {
                // Read the response body for more details
                responseBody = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
                log.error("Response Body: {}", responseBody);
            }
        } catch (IOException e) {
            log.error("Failed to read error response body for method: {}. Error: {}", methodKey, e.getMessage());
            // Fallback to default if body can't be read
            return defaultErrorDecoder.decode(methodKey, response);
        }
        final String detailedMessage = responseBody != null && !responseBody.isEmpty()
                ? "External service error: " + responseBody
                : "External service error.";

        return switch (response.status()) {
            case 400 -> // Bad Request
                    new AppException(ErrorCode.EXTERNAL_SERVICE_BAD_REQUEST, detailedMessage);
            case 503 -> // Service Unavailable
                    new AppException(ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE, detailedMessage);
            default ->
                // For any other status code, use the default decoder or a generic exception
                    defaultErrorDecoder.decode(methodKey, response);
        };
    }
}
