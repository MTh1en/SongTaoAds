package com.capstone.ads.service;

import com.capstone.ads.dto.payment.PaymentDTO;
import org.springframework.data.domain.Page;
import vn.payos.type.*;

public interface PaymentService {
    CheckoutResponseData createConstructionDepositPaymentLink(String orderId) throws Exception;

    CheckoutResponseData createConstructionRemainingPaymentLink(String orderId) throws Exception;

    CheckoutResponseData createCustomDesignFullDepositPaymentLink(String orderId) throws Exception;

    CheckoutResponseData createCustomDesignFullRemainingPaymentLink(String orderId) throws Exception;

    CheckoutResponseData createFullOrderPaymentLink(String orderId) throws Exception;

    WebhookData verifyPaymentWebhookData(Webhook Webhook) throws Exception;

    void updateStatusByWebhookData(Webhook Webhook);

    String confirmWebhookUrl(String webhookUrl) throws Exception;

    PaymentLinkData getPaymentLinkInformation(Long orderCode) throws Exception;

    void cancelPayment(Long paymentCode);

    Page<PaymentDTO> findPaymentByOrderId(String orderId, int page, int size);

    Page<PaymentDTO> findPaymentByUserId(String userId, int page, int size);
}
