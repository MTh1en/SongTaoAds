package com.capstone.ads.service.impl;

import com.capstone.ads.dto.payment.CreatePaymentRequest;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.*;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {


    private static final double DEPOSIT_PERCENTAGE = 0.3;
    private static final double REMAINING_PERCENTAGE = 0.7;

    @Value("${payos.CLIENT_ID}")
    private String CLIENT_ID;

    @Value("${payos.API_KEY}")
    private String API_KEY;

    @Value("${payos.CHECKSUM_KEY}")
    private String CHECKSUM_KEY;

    @Value("${payos.return-url}")
    private String RETURN_URL;

    @Value("${payos.cancel-url}")
    private String CANCEL_URL;

    private final SecureRandom random = new SecureRandom();
    private final PaymentsRepository paymentRepository;
    private final OrdersRepository orderRepository;
    private final OrderService orderService;

    @Override
    public CheckoutResponseData createDepositPaymentLink(CreatePaymentRequest request) throws Exception {
        log.info("Creating payment link for deposit (30%) for order: {}", request.getOrderId());

        PayOS payOS = new PayOS(CLIENT_ID, API_KEY, CHECKSUM_KEY);

        // Generate a numeric order code
        long orderCode = Math.abs(random.nextLong() % 10000000000L); // 10-digit numeric code

        Orders order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getTotalAmount() == null || order.getUsers() == null) {
            log.error("Invalid order data: totalAmount or users is null for order {}", request.getOrderId());
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }

        // Calculate deposit (30% of totalAmount) using double
        double totalAmount = order.getTotalAmount();
        if (totalAmount <= 0) {
            log.error("Invalid totalAmount for order {}: {}", request.getOrderId(), totalAmount);
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        double depositAmount = Math.round(totalAmount * DEPOSIT_PERCENTAGE * 100.0) / 100.0;
        order.setDepositAmount(depositAmount);

        // Create ItemData (amount in VND, no decimals)
        int amountInVnd = (int) Math.round(depositAmount); // Round to nearest integer for VND
        ItemData itemData = ItemData.builder()
                .name("Deposit for Order of - " + order.getUsers().getFullName())
                .price(amountInVnd)
                .quantity(1)
                .build();

        // Create PaymentData
        PaymentData paymentData = PaymentData.builder()
                .orderCode(orderCode)
                .amount(amountInVnd)
                .description(request.getDescription())
                .returnUrl(RETURN_URL)
                .cancelUrl(CANCEL_URL + "/" + orderCode)
                .item(itemData)
                .build();

        // Create and save Payment entity
        Payments payment = Payments.builder()
                .totalAmount(depositAmount)
                .paymentMethod(PaymentMethod.PAYOS)
                .paymentStatus(PaymentStatus.PENDING_DEPOSITED)
                .paymentDate(LocalDateTime.now())
                .isDeposit(true)
                .orders(order)
                .payOsPaymentLinkId(String.valueOf(orderCode)) // Store numeric orderCode as string
                .build();
        payment = paymentRepository.save(payment);

        // Update order's payments list
        if (order.getPayments() == null) {
            order.setPayments(new ArrayList<>());
        }
        order.getPayments().add(payment);
        orderRepository.save(order);

        // Create payment link
        CheckoutResponseData response = payOS.createPaymentLink(paymentData);

        // Update payment with PayOS transaction ID
        payment.setPayOsPaymentLinkId(response.getPaymentLinkId());
        paymentRepository.save(payment);

        log.info("Created deposit payment link for order {}: payOsPaymentLinkId={}, paymentUrl={}",
                order.getId(), response.getPaymentLinkId(), response.getCheckoutUrl());

        return response;
    }

    @Override
    public CheckoutResponseData createRemainingPaymentLink(CreatePaymentRequest request) throws Exception {
        log.info("Creating payment link for remaining amount (70%) for order: {}", request.getOrderId());

        PayOS payOS = new PayOS(CLIENT_ID, API_KEY, CHECKSUM_KEY);

        // Generate a numeric order code
        long orderCode = Math.abs(random.nextLong() % 10000000000L); // 10-digit numeric code

        Orders order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getTotalAmount() == null || order.getUsers() == null) {
            log.error("Invalid order data: totalAmount or users is null for order {}", request.getOrderId());
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        // Check if order status is DEPOSITED
        if (order.getStatus() != OrderStatus.DEPOSITED) {
            log.error("Order {} is not in DEPOSITED status, current status: {}", request.getOrderId(), order.getStatus());
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        }

        // Calculate remaining amount (70% of totalAmount) using double
        double totalAmount = order.getTotalAmount();
        if (totalAmount <= 0) {
            log.error("Invalid totalAmount for order {}: {}", request.getOrderId(), totalAmount);
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        double remainingAmount = Math.round(totalAmount * REMAINING_PERCENTAGE * 100.0) / 100.0;

        // Create ItemData (amount in VND, no decimals)
        int amountInVnd = (int) Math.round(remainingAmount); // Round to nearest integer for VND
        ItemData itemData = ItemData.builder()
                .name("Remaining Payment for Order of - " + order.getUsers().getFullName())
                .price(amountInVnd)
                .quantity(1)
                .build();

        // Create PaymentData
        PaymentData paymentData = PaymentData.builder()
                .orderCode(orderCode)
                .amount(amountInVnd)
                .description(request.getDescription())
                .returnUrl(RETURN_URL)
                .cancelUrl(CANCEL_URL + "/" + orderCode)
                .item(itemData)
                .build();

        // Create and save Payment entity
        Payments payment = Payments.builder()
                .totalAmount(remainingAmount)
                .paymentMethod(PaymentMethod.PAYOS)
                .paymentStatus(PaymentStatus.PENDING_REMAINDING)
                .paymentDate(LocalDateTime.now())
                .isDeposit(false)
                .orders(order)
                .payOsPaymentLinkId(String.valueOf(orderCode)) // Store numeric orderCode as string
                .build();
        payment = paymentRepository.save(payment);

        // Update order's payments list
        if (order.getPayments() == null) {
            order.setPayments(new ArrayList<>());
        }
        order.getPayments().add(payment);
        orderRepository.save(order);

        // Create payment link
        CheckoutResponseData response = payOS.createPaymentLink(paymentData);

        // Update payment with PayOS transaction ID
        payment.setPayOsPaymentLinkId(response.getPaymentLinkId());
        paymentRepository.save(payment);

        log.info("Created remaining payment link for order {}: payOsPaymentLinkId={}, paymentUrl={}",
                order.getId(), response.getPaymentLinkId(), response.getCheckoutUrl());

        return response;
    }

    @Override
    public void handlePayOsCallback(String payOsPaymentLinkId, String payOsStatus) {
        Payments payment = paymentRepository.findByPayOsPaymentLinkId(payOsPaymentLinkId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));

        PaymentStatus paymentStatus;
        try {
            paymentStatus = PaymentStatus.valueOf(payOsStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Invalid payment status in callback: {}", payOsStatus);
            throw new AppException(ErrorCode.INVALID_STATUS_PAYMENT);
        }

        payment.setPaymentStatus(paymentStatus);
        paymentRepository.save(payment);

        Orders order = payment.getOrders();
        if (paymentStatus == PaymentStatus.SUCCESS) {
            if (payment.getIsDeposit()) {
                orderService.UpdateOrderStatus(order.getId(), OrderStatus.DEPOSITED.name());
            } else {
                orderService.UpdateOrderStatus(order.getId(), OrderStatus.COMPLETED.name());
            }
        } else if (paymentStatus == PaymentStatus.CANCELLED || paymentStatus == PaymentStatus.EXPIRED) {
            orderService.UpdateOrderStatus(order.getId(), OrderStatus.CANCELLED.name());
        }
    }


}