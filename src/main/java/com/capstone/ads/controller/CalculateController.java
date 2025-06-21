package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.service.CalculateService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CalculateController {
    private final CalculateService service;

    @PostMapping("/customer-choices-detail/{customerChoiceDetailId}/subtotal")
    public ApiResponse<Long> subtotal(@PathVariable String customerChoiceDetailId) {
        var result = service.calculateSubtotal(customerChoiceDetailId);
        return ApiResponseBuilder.buildSuccessResponse("Calculate subtotal successful", result);
    }

    @PostMapping("/customer-choices/{customerChoiceId}/total")
    public ApiResponse<Long> total(@PathVariable String customerChoiceId) {
        var result = service.calculateTotal(customerChoiceId);
        return ApiResponseBuilder.buildSuccessResponse("Calculate total successful", result);
    }
}
