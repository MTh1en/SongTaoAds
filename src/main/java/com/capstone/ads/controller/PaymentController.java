package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.payment.CreatePaymentRequest;
import com.capstone.ads.service.PaymentService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.payos.type.CheckoutResponseData;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/deposit")
    public ApiResponse<CheckoutResponseData> createPaymentDepositAmount(@RequestBody CreatePaymentRequest request) throws Exception {
        CheckoutResponseData response = paymentService.createDepositPaymentLink(request);
        return ApiResponseBuilder.buildSuccessResponse("Payment initiated", response);
    }

    @PostMapping("/remaining")
    public ApiResponse<CheckoutResponseData> createPaymentRemainingAmount(@RequestBody CreatePaymentRequest request) throws Exception {
        CheckoutResponseData response = paymentService.createRemainingPaymentLink(request);
        return ApiResponseBuilder.buildSuccessResponse("Payment initiated", response);
    }

//    @GetMapping("/callback")
//    public ApiResponse<String> payOsCallback(
//            @RequestParam String paymentLinkId,
//            @RequestParam String paymentStatus) {
//        try {
//            paymentService.handlePayOsCallback(paymentLinkId, paymentStatus);
//            return ApiResponseBuilder.buildSuccessResponse("Callback processed successfully", null);
//        } catch (Exception e) {
//            // Return an ApiResponse indicating failure
//            return ApiResponseBuilder.buildSuccessResponse("Error processing callback: " + e.getMessage(), null);
//        }
//    }
}