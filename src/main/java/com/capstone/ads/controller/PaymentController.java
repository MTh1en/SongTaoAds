package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.service.PaymentService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
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
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/orders/{orderId}/deposit")
    public ApiResponse<CheckoutResponseData> createOrderDepositPaymentLink(@PathVariable String orderId) throws Exception {
        CheckoutResponseData response = paymentService.createOrderDepositPaymentLink(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Payment initiated", response);
    }

    @PostMapping("/orders/{orderId}/remaining")
    public ApiResponse<CheckoutResponseData> createOrderRemainingPaymentLink(@PathVariable String orderId) throws Exception {
        CheckoutResponseData response = paymentService.createOrderRemainingPaymentLink(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Payment initiated", response);
    }

    @PostMapping("/custom-design-requests/{customDesignRequestId}/deposit")
    public ApiResponse<CheckoutResponseData> createCustomDesignRequestDepositPaymentLink(@PathVariable String customDesignRequestId) throws Exception {
        CheckoutResponseData response = paymentService.createCustomDesignRequestDepositPaymentLink(customDesignRequestId);
        return ApiResponseBuilder.buildSuccessResponse("Payment initiated", response);
    }

    @PostMapping("/custom-design-requests/{customDesignRequestId}/remaining")
    public ApiResponse<CheckoutResponseData> createCustomDesignRequestRemainingPaymentLink(@PathVariable String customDesignRequestId) throws Exception {
        CheckoutResponseData response = paymentService.createCustomDesignRequestRemainingPaymentLink(customDesignRequestId);
        return ApiResponseBuilder.buildSuccessResponse("Payment initiated", response);
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