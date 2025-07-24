package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.progress_log.ProgressLogCreateRequest;
import com.capstone.ads.dto.progress_log.ProgressLogDTO;
import com.capstone.ads.service.ProgressLogService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "PROGRESS LOG")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProgressLogController {
    ProgressLogService progressLogService;

    @PostMapping(value = "/orders/{orderId}/progress-logs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProgressLogDTO> createProgressLog(@PathVariable String orderId,
                                                         @ModelAttribute ProgressLogCreateRequest request) {
        var response = progressLogService.createProgressLog(orderId, request);
        return ApiResponseBuilder.buildSuccessResponse("Progress log created", response);
    }

    @GetMapping(value = "/orders/{orderId}/progress-logs")
    public ApiPagingResponse<ProgressLogDTO> findProgressLogByOrderId(
            @PathVariable String orderId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = progressLogService.findProgressLogByOrderId(orderId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Progress log by orderId", response, page);
    }
}
