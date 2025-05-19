package com.capstone.ads.service;

import com.capstone.ads.dto.payment.CreatePaymentRequestDTO;
import com.capstone.ads.dto.payment.CreatePaymentResponseDTO;

import vn.payos.type.CheckoutResponseData;
import vn.payos.type.PaymentLinkData;

public interface PaymentService {
    CheckoutResponseData createPaymentLink(CreatePaymentRequestDTO request) throws Exception;
    PaymentLinkData checkPaymentStatus(String orderId) throws Exception;
    PaymentLinkData cancelPaymentLink(String orderId) throws Exception;
    void cancelPayment(String orderId);
}
