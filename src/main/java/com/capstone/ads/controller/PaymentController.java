package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.payment.CreatePaymentRequest;
import com.capstone.ads.service.PaymentService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.Webhook;
import vn.payos.type.WebhookData;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/payments/deposit")
    public ApiResponse<CheckoutResponseData> createPaymentDepositAmount(@RequestBody CreatePaymentRequest request) throws Exception {
        CheckoutResponseData response = paymentService.createDepositPaymentLink(request);
        return ApiResponseBuilder.buildSuccessResponse("Payment initiated", response);
    }

    @PostMapping("/payments/remaining")
    public ApiResponse<CheckoutResponseData> createPaymentRemainingAmount(@RequestBody CreatePaymentRequest request) throws Exception {
        CheckoutResponseData response = paymentService.createRemainingPaymentLink(request);
        return ApiResponseBuilder.buildSuccessResponse("Payment initiated", response);
    }

    @GetMapping("orders/{orderId}/callback")
    public ApiResponse<Void> payOsCallback(@PathVariable String orderId) {
        paymentService.handlePayOsCallback(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Callback processed successfully", null);
    }

    @PostMapping("/payments/handle-webhook")
    public ApiResponse<String> handleWebHook(@RequestBody Webhook webhook) throws Exception {
        WebhookData webhookData = paymentService.verifyPaymentWebhookData(webhook);
        paymentService.updateOrderStatusByWebhookData(webhookData);
        return ApiResponseBuilder.buildSuccessResponse("Handle callback successfully", null);
    }
}