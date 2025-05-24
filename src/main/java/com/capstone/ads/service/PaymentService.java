package com.capstone.ads.service;

import com.capstone.ads.dto.payment.CreatePaymentRequest;

import vn.payos.type.CheckoutResponseData;

public interface PaymentService {
    CheckoutResponseData createDepositPaymentLink(CreatePaymentRequest request) throws Exception;

    CheckoutResponseData createRemainingPaymentLink(CreatePaymentRequest request) throws Exception;

    void handlePayOsCallback(String orderId);
}
