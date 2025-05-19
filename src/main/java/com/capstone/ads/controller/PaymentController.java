package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.payment.CreatePaymentRequestDTO;
import com.capstone.ads.dto.payment.CreatePaymentResponseDTO;
import com.capstone.ads.service.PaymentService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.PaymentLinkData;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ApiResponse<CheckoutResponseData> createPayment(@RequestBody CreatePaymentRequestDTO request) throws Exception {
        CheckoutResponseData response = paymentService.createPaymentLink(request);
        return ApiResponseBuilder.buildSuccessResponse("Payment initiated", response);
    }

    @GetMapping("/check/{orderId}")
    public ApiResponse<PaymentLinkData> checkPaymentStatus(@PathVariable String orderId) throws Exception {
        PaymentLinkData paymentLinkData = paymentService.checkPaymentStatus(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Payment status checked", paymentLinkData);
    }

    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<Void> cancelPayment(@PathVariable String orderId) throws Exception {
        paymentService.cancelPaymentLink(orderId);
        paymentService.cancelPayment(orderId);
        return ResponseEntity.ok().build();
    }
}