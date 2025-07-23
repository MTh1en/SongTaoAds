package com.capstone.ads.service;

import vn.payos.type.CheckoutResponseData;
import vn.payos.type.Webhook;
import vn.payos.type.WebhookData;

public interface PaymentService {
    CheckoutResponseData createConstructionDepositPaymentLink(String orderId) throws Exception;

    CheckoutResponseData createConstructionRemainingPaymentLink(String orderId) throws Exception;

    CheckoutResponseData createCustomDesignFullDepositPaymentLink(String orderId) throws Exception;

    CheckoutResponseData createCustomDesignFullRemainingPaymentLink(String orderId) throws Exception;

    CheckoutResponseData createFullOrderPaymentLink(String orderId) throws Exception;

    WebhookData verifyPaymentWebhookData(Webhook Webhook) throws Exception;

    void updateStatusByWebhookData(Webhook Webhook);

    String confirmWebhookUrl(String webhookUrl) throws Exception;

    void cancelPayment(Long paymentCode);
}
