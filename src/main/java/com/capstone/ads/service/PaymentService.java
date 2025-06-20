package com.capstone.ads.service;

import vn.payos.type.CheckoutResponseData;
import vn.payos.type.Webhook;
import vn.payos.type.WebhookData;

public interface PaymentService {
    CheckoutResponseData createOrderDepositPaymentLink(String orderId) throws Exception;

    CheckoutResponseData createOrderRemainingPaymentLink(String orderId) throws Exception;

    CheckoutResponseData createCustomDesignRequestDepositPaymentLink(String orderId) throws Exception;

    CheckoutResponseData createCustomDesignRequestRemainingPaymentLink(String orderId) throws Exception;

    WebhookData verifyPaymentWebhookData(Webhook Webhook) throws Exception;

    void updateStatusByWebhookData(Webhook Webhook);

    String confirmWebhookUrl(String webhookUrl) throws Exception;

    void cancelPayment(Long paymentCode);
}
