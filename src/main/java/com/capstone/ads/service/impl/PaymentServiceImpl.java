package com.capstone.ads.service.impl;

import com.capstone.ads.dto.payment.CreatePaymentRequestDTO;
import com.capstone.ads.dto.payment.CreatePaymentResponseDTO;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.model.Orders;
import com.capstone.ads.model.Payments;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.model.enums.PaymentMethod;
import com.capstone.ads.model.enums.PaymentStatus;
import com.capstone.ads.repository.internal.OrdersRepository;
import com.capstone.ads.repository.internal.PaymentsRepository;
import com.capstone.ads.service.OrderService;
import com.capstone.ads.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.*;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Value("${payos.client-id}")
    private String CLIENT_ID;

    @Value("${payos.api-key}")
    private String API_KEY;

    @Value("${payos.checksum-key}")
    private String CHECKSUM_KEY;

    @Value("${payos.create-paymenturl}")
    private String CREATE_PAYMENT_URL;

    @Value("${payos.check-paymenturl}")
    private String CHECK_PAYMENT_URL;

    @Value("${payos.return-url}")
    private String RETURN_URL;

    @Value("${payos.cancel-url}")
    private String CANCEL_URL;

    private final Random random = new SecureRandom();

    private final PaymentsRepository paymentRepository;
    private final OrdersRepository orderRepository;
    private final OrderService orderService;

    @Override
    public CheckoutResponseData createPaymentLink(CreatePaymentRequestDTO request) throws Exception {
        logger.info("Creating payment link for user: {}", request.getOrderId());

        PayOS payOS = new PayOS(CLIENT_ID, API_KEY, CHECKSUM_KEY);

        String orderCode = String.valueOf(Math.abs(random.nextLong() % 10_000_000_000L));

        Orders order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        ItemData itemData = ItemData.builder()
                .name("Order of - " + order.getUsers().getFullName())
                .price((int)order.getTotalAmount().longValue())
                .quantity(1)
                .build();

        PaymentData paymentData = PaymentData.builder()
                .orderCode(Long.parseLong(orderCode))
                .amount(itemData.getPrice())
                .description(request.getDescription())
                .returnUrl(RETURN_URL)
                .cancelUrl(CANCEL_URL + "/" + orderCode)
                .item(itemData)
                .build();

        Payments payment = Payments.builder()
                .totalAmount(order.getTotalAmount())
                .paymentMethod(PaymentMethod.PAYOS)
                .paymentStatus(PaymentStatus.PENDING)
                .paymentDate(LocalDateTime.now())
                .isDeposit(false)
                .orders(order)
                .build();
        payment = paymentRepository.save(payment);

        if (order.getPayments() == null) {
            order.setPayments(new java.util.ArrayList<>());
        }
        order.getPayments().add(payment);
        orderRepository.save(order);

        CheckoutResponseData response = payOS.createPaymentLink(paymentData);

        // Update payment with PayOS transaction ID
        payment.setPayOsPaymentLinkId(response.getPaymentLinkId());
        paymentRepository.save(payment);

        logger.info("Created payment link for order {}: payOsPaymentLinkId={}, paymentUrl={}",
                order.getId(), response.getPaymentLinkId(), response.getCheckoutUrl());

        return response; // Return the CheckoutResponseData directly
    }

    @Override
    public PaymentLinkData checkPaymentStatus(String orderId) throws Exception {
        logger.info("Checking payment status for order: {}", orderId);

        PayOS payOS = new PayOS(CLIENT_ID, API_KEY, CHECKSUM_KEY);
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        Payments payment = paymentRepository.findByIdAndOrdersId(orderId, orderId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));

        // Check payment status with PayOS
        PaymentLinkData paymentLinkData = payOS.getPaymentLinkInformation(Long.parseLong(payment.getPayOsPaymentLinkId()));
        String status = paymentLinkData.getStatus().toUpperCase();

        // Validate payment status
        PaymentStatus paymentStatus;
        try {
            paymentStatus = PaymentStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid payment status received from PayOS for order {}: {}", orderId, status);
            throw new AppException(ErrorCode.INVALID_STATUS_PAYMENT);
        }

        payment.setPaymentStatus(paymentStatus);
        paymentRepository.save(payment);
        logger.info("Updated payment status for order {}: {}", orderId, paymentStatus);

        if ("SUCCESS".equals(status)) {
            orderService.UpdateOrderStatus(order.getId(), OrderStatus.DEPOSITED.name());
            logger.info("Order {} status updated to PAID due to successful payment", orderId);
        } else if ("FAILED".equals(status) || "CANCELLED".equals(status)) {
            orderService.UpdateOrderStatus(order.getId(), OrderStatus.CANCELLED.name());
            logger.info("Order {} status updated to CANCELLED due to payment status: {}", orderId, status);
        } else {
            logger.warn("Unexpected payment status for order {}: {}. No order status update performed.", orderId, status);
        }

        return paymentLinkData;
    }

    @Override
    public PaymentLinkData cancelPaymentLink(String orderId) throws Exception {
        logger.info("Canceling payment link for order: {}", orderId);

        PayOS payOS = new PayOS(CLIENT_ID, API_KEY, CHECKSUM_KEY);
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        Payments payment = paymentRepository.findByIdAndOrdersId(orderId, orderId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
        PaymentLinkData canceledPayment = payOS.cancelPaymentLink(Long.parseLong(payment.getPayOsPaymentLinkId()), "Order cancelled");

        logger.info("Payment link canceled for order {}: payOsPaymentLinkId={}", orderId, payment.getPayOsPaymentLinkId());
        return canceledPayment;
    }

    @Override
    public void cancelPayment(String orderId) {
        logger.info("Canceling payment for order: {}", orderId);

        Payments payment = paymentRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
        payment.setPaymentStatus(PaymentStatus.CANCELLED);
        paymentRepository.save(payment);
        Orders order = payment.getOrders();
        orderService.UpdateOrderStatus(order.getId(), OrderStatus.CANCELLED.name());

        logger.info("Payment canceled and order {} status updated to CANCELLED", order.getId());
    }
}