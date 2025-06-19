package com.capstone.ads.service.impl;

import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.model.CustomDesignRequests;
import com.capstone.ads.model.Orders;
import com.capstone.ads.model.Payments;
import com.capstone.ads.model.enums.PaymentMethod;
import com.capstone.ads.model.enums.PaymentStatus;
import com.capstone.ads.repository.internal.PaymentsRepository;
import com.capstone.ads.service.CustomDesignRequestService;
import com.capstone.ads.service.OrderService;
import com.capstone.ads.service.PaymentService;
import com.capstone.ads.utils.DataConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.payos.PayOS;
import vn.payos.type.*;

import java.security.SecureRandom;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    @Value("${payos.CLIENT_ID}")
    private String CLIENT_ID;

    @Value("${payos.API_KEY}")
    private String API_KEY;

    @Value("${payos.CHECKSUM_KEY}")
    private String CHECKSUM_KEY;

    @Value("${app.base.url}")
    private String BASE_URL;

    private final SecureRandom random = new SecureRandom();
    private final PaymentsRepository paymentRepository;
    private final OrderService orderService;
    private final CustomDesignRequestService customDesignRequestService;

    @Override
    public CheckoutResponseData createOrderDepositPaymentLink(String orderId) throws Exception {
        Orders order = orderService.getOrderById(orderId);
        return createPaymentLinkForOrder(order, true);
    }

    @Override
    public CheckoutResponseData createOrderRemainingPaymentLink(String orderId) throws Exception {
        Orders order = orderService.getOrderById(orderId);
        return createPaymentLinkForOrder(order, false);
    }

    @Override
    public CheckoutResponseData createCustomDesignRequestDepositPaymentLink(String orderId) throws Exception {
        CustomDesignRequests customDesignRequests = customDesignRequestService.getCustomDesignRequestById(orderId);
        return createPaymentLinkForCustomDesignRequest(customDesignRequests, true);
    }

    @Override
    public CheckoutResponseData createCustomDesignRequestRemainingPaymentLink(String orderId) throws Exception {
        CustomDesignRequests customDesignRequests = customDesignRequestService.getCustomDesignRequestById(orderId);
        return createPaymentLinkForCustomDesignRequest(customDesignRequests, false);
    }


    @Override
    public WebhookData verifyPaymentWebhookData(Webhook Webhook) throws Exception {
        PayOS payOS = new PayOS(CLIENT_ID, API_KEY, CHECKSUM_KEY);
        return payOS.verifyPaymentWebhookData(Webhook);
    }

    @Override
    @Transactional
    public void updateStatusByWebhookData(WebhookData webhookData) {
        Payments payment = paymentRepository.findByCode(webhookData.getOrderCode())
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
        boolean isDeposit = payment.getIsDeposit();
        if (webhookData.getCode().equals("00")) {
            payment.setStatus(PaymentStatus.SUCCESS);
            if (!Objects.isNull(payment.getCustomDesignRequests())) {
                CustomDesignRequests customDesignRequests = payment.getCustomDesignRequests();
                customDesignRequestService.updateCustomDesignRequestFromWebhookResult(customDesignRequests, isDeposit);
            }
            if (!Objects.isNull(payment.getOrders())) {
                Orders orders = payment.getOrders();
                //Method update order
            }
        } else {
            payment.setStatus(PaymentStatus.FAILED);
        }
        paymentRepository.save(payment);
    }

    @Override
    public String confirmWebhookUrl(String webhookUrl) throws Exception {
        PayOS payOS = new PayOS(CLIENT_ID, API_KEY, CHECKSUM_KEY);
        return payOS.confirmWebhook(webhookUrl);
    }

    @Override
    @Transactional
    public void cancelPayment(Long paymentCode) {
        Payments payments = paymentRepository.findByCode(paymentCode)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
        payments.setStatus(PaymentStatus.CANCELLED);
        paymentRepository.save(payments);
    }

    private CheckoutResponseData createPaymentLinkForOrder(Orders order, boolean isDeposit) throws Exception {
        PayOS payOS = new PayOS(CLIENT_ID, API_KEY, CHECKSUM_KEY);
        long paymentCode = generateOrderCode();

        double amount = (isDeposit)
                ? order.getDepositAmount()
                : order.getRemainingAmount();
        int payOsAmount = DataConverter.convertDoubleToInt(amount);

        // Create PaymentData
        PaymentData paymentData = PaymentData.builder()
                .orderCode(paymentCode)
                .description("Orders")
                .amount(payOsAmount)
                .returnUrl(BASE_URL + "/api/payments/success")
                .cancelUrl(BASE_URL + "/api/payments/fail/" + paymentCode)
                .build();

        // Create and save Payment entity
        Payments payment = Payments.builder()
                .code(paymentCode)
                .amount(payOsAmount)
                .method(PaymentMethod.PAYOS)
                .status(PaymentStatus.PENDING)
                .isDeposit(isDeposit)
                .orders(order)
                .build();
        paymentRepository.save(payment);

        // Create payment link
        return payOS.createPaymentLink(paymentData);
    }

    private CheckoutResponseData createPaymentLinkForCustomDesignRequest(CustomDesignRequests customDesignRequests, boolean isDeposit) throws Exception {
        PayOS payOS = new PayOS(CLIENT_ID, API_KEY, CHECKSUM_KEY);

        long paymentCode = generateOrderCode();

        int payOsAmount = (isDeposit)
                ? customDesignRequests.getDepositAmount()
                : customDesignRequests.getRemainingAmount();
        // Create PaymentData
        PaymentData paymentData = PaymentData.builder()
                .orderCode(paymentCode)
                .description("Custom Design Request")
                .amount(payOsAmount)
                .returnUrl(BASE_URL + "/api/payments/success")
                .cancelUrl(BASE_URL + "/api/payments/fail/" + paymentCode)
                .build();

        // Create and save Payment entity
        Payments payment = Payments.builder()
                .code(paymentCode)
                .amount(payOsAmount)
                .method(PaymentMethod.PAYOS)
                .status(PaymentStatus.PENDING)
                .isDeposit(isDeposit)
                .customDesignRequests(customDesignRequests)
                .build();
        paymentRepository.save(payment);

        // Create payment link
        return payOS.createPaymentLink(paymentData);
    }

    private long generateOrderCode() {
        return Math.abs(random.nextLong() % 10000000000L);
    }
}