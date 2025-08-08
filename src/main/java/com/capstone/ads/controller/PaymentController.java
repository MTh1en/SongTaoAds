package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.payment.PaymentDTO;
import com.capstone.ads.service.PaymentService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.Webhook;
import vn.payos.type.WebhookData;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "PAYMENT")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
    PaymentService paymentService;

    @PostMapping("/orders/{orderId}/deposit")
    @Operation(summary = "Đặt cọc theo đơn hàng")
    public ApiResponse<CheckoutResponseData> createOrderDepositPaymentLink(@PathVariable String orderId) throws Exception {
        CheckoutResponseData response = paymentService.createConstructionDepositPaymentLink(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Tạo link đặt cọc đơn hàng thành công", response);
    }

    @PostMapping("/orders/{orderId}/remaining")
    @Operation(summary = "Thanh toán hết đơn hàng")
    public ApiResponse<CheckoutResponseData> createOrderRemainingPaymentLink(@PathVariable String orderId) throws Exception {
        CheckoutResponseData response = paymentService.createConstructionRemainingPaymentLink(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Tạo link thanh toán hết đơn hàng thành công", response);
    }

    @PostMapping("/orders/{orderId}/design-deposit")
    @Operation(summary = "Đặt cọc theo thiết kế")
    public ApiResponse<CheckoutResponseData> createCustomDesignFullDepositPaymentLink(@PathVariable String orderId) throws Exception {
        CheckoutResponseData response = paymentService.createCustomDesignFullDepositPaymentLink(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Tạo link đặt cọc thiết kế thành công", response);
    }

    @PostMapping("/orders/{orderId}/design-remaining")
    @Operation(summary = "Thanh toán hết thiết kế")
    public ApiResponse<CheckoutResponseData> createCustomDesignFullRemainingPaymentLink(@PathVariable String orderId) throws Exception {
        CheckoutResponseData response = paymentService.createCustomDesignFullRemainingPaymentLink(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Tạo link thanh toán hết thiết kế thành công", response);
    }

    @PostMapping("/webhook/handle-webhook")
    public WebhookData handleWebHook(@RequestBody Webhook webhook) throws Exception {
        WebhookData webhookData = paymentService.verifyPaymentWebhookData(webhook);
        log.info("Webhook verified: {}", webhook.getCode());
        log.info("Webhook verified: {}", webhook.getSuccess());
        log.info("Webhook verified: {}", webhook.getData());
        log.info("Webhook verified: {}", webhook.getDesc());

        paymentService.updateStatusByWebhookData(webhook);
        return webhookData;
    }

    @PostMapping("/webhook/confirm-webhook-url")
    public ApiResponse<String> registerWebhookUrl(@RequestBody String webhookUrl) throws Exception {
        String result = paymentService.confirmWebhookUrl(webhookUrl);
        return ApiResponseBuilder.buildSuccessResponse("Register WebhookUrl successfully", result);
    }

    @GetMapping("/orders/{orderId}/payments")
    @Operation(summary = "Xem lịch sử thanh toán theo đơn hàng")
    public ApiPagingResponse<PaymentDTO> findPaymentByOrderId(
            @PathVariable String orderId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = paymentService.findPaymentByOrderId(orderId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse(
                "Xem lịch sử thanh toán theo đơn hàng thành công",
                response, page);
    }

    @GetMapping("/users/{userId}/payments")
    @Operation(summary = "Xem lịch sử thanh toán theo đơn hàng")
    public ApiPagingResponse<PaymentDTO> findPaymentByUserId(
            @PathVariable String userId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = paymentService.findPaymentByUserId(userId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse(
                "Xem lịch sử thanh toán theo tài khoản thành công",
                response, page);
    }

    //Template Engine
    @GetMapping("/payments/success")
    public ModelAndView checkoutSuccess() {
        return new ModelAndView("PaymentSuccess");
    }

    @GetMapping("/payments/fail/{paymentCode}")
    public ModelAndView checkoutFail(@PathVariable Long paymentCode) {
        paymentService.cancelPayment(paymentCode);
        return new ModelAndView("PaymentFailure");
    }
}