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
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private static final double DEPOSIT_PERCENTAGE = 0.3;
    private static final double REMAINING_PERCENTAGE = 0.7;

    @Value("${payos.client-id}")
    private String CLIENT_ID;

    @Value("${payos.api-key}")
    private String API_KEY;

    @Value("${payos.checksum-key}")
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
    public CheckoutResponseData createDepositPaymentLink(CreatePaymentRequestDTO request) throws Exception {
        logger.info("Creating payment link for deposit (30%) for order: {}", request.getOrderId());

        PayOS payOS = new PayOS(CLIENT_ID, API_KEY, CHECKSUM_KEY);

        // Generate a unique order code
        String orderCode = UUID.randomUUID().toString().replace("-", "").substring(0, 10);

        Orders order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getTotalAmount() == null || order.getUsers() == null) {
            logger.error("Invalid order data: totalAmount or users is null for order {}", request.getOrderId());
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }

        // Calculate deposit (30% of totalAmount) using double
        double totalAmount = order.getTotalAmount();
        if (totalAmount <= 0) {
            logger.error("Invalid totalAmount for order {}: {}", request.getOrderId(), totalAmount);
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        double depositAmount = Math.round(totalAmount * DEPOSIT_PERCENTAGE * 100.0) / 100.0;
        order.setDepositAmount(depositAmount);

        // Create ItemData
        ItemData itemData = ItemData.builder()
                .name("Deposit for Order of - " + order.getUsers().getFullName())
                .price((int) depositAmount) // Convert to int for ItemData.price
                .quantity(1)
                .build();

        // Create PaymentData
        PaymentData paymentData = PaymentData.builder()
                .orderCode(Long.parseLong(orderCode))
                .amount(itemData.getPrice())
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

        logger.info("Created deposit payment link for order {}: payOsPaymentLinkId={}, paymentUrl={}",
                order.getId(), response.getPaymentLinkId(), response.getCheckoutUrl());

        return response;
    }

    @Override
    public CheckoutResponseData createRemainingPaymentLink(CreatePaymentRequestDTO request) throws Exception {
        logger.info("Creating payment link for remaining amount (70%) for order: {}", request.getOrderId());

        PayOS payOS = new PayOS(CLIENT_ID, API_KEY, CHECKSUM_KEY);

        // Generate a unique order code
        String orderCode = UUID.randomUUID().toString().replace("-", "").substring(0, 10);

        Orders order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getTotalAmount() == null || order.getUsers() == null) {
            logger.error("Invalid order data: totalAmount or users is null for order {}", request.getOrderId());
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        // Check if order status is DEPOSITED
        if (order.getStatus() != OrderStatus.DEPOSITED) {
            logger.error("Order {} is not in DEPOSITED status, current status: {}", request.getOrderId(), order.getStatus());
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        }

        // Calculate remaining amount (70% of totalAmount) using double
        double totalAmount = order.getTotalAmount();
        if (totalAmount <= 0) {
            logger.error("Invalid totalAmount for order {}: {}", request.getOrderId(), totalAmount);
                throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        double remainingAmount = Math.round(totalAmount * REMAINING_PERCENTAGE * 100.0) / 100.0;

        // Create ItemData
        ItemData itemData = ItemData.builder()
                .name("Remaining Payment for Order of - " + order.getUsers().getFullName())
                .price((int) remainingAmount) // Convert to int for ItemData.price
                .quantity(1)
                .build();

        // Create PaymentData
        PaymentData paymentData = PaymentData.builder()
                .orderCode(Long.parseLong(orderCode))
                .amount(itemData.getPrice())
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

        logger.info("Created remaining payment link for order {}: payOsPaymentLinkId={}, paymentUrl={}",
                order.getId(), response.getPaymentLinkId(), response.getCheckoutUrl());

        return response;
    }

    @Override
    public PaymentLinkData checkPaymentStatus(String orderId) throws Exception {
        logger.info("Checking payment status for order: {}", orderId);

        PayOS payOS = new PayOS(CLIENT_ID, API_KEY, CHECKSUM_KEY);
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        Payments payment = paymentRepository.findOrdersId(orderId)
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

        // Update order status based on payment status
        switch (status) {
            case "PAID":
                if (payment.getIsDeposit()) {
                    orderService.UpdateOrderStatus(order.getId(), OrderStatus.DEPOSITED.name());
                    logger.info("Order {} status updated to DEPOSITED due to successful deposit payment", orderId);
                } else {
                    orderService.UpdateOrderStatus(order.getId(), OrderStatus.COMPLETED.name());
                    logger.info("Order {} status updated to PAID due to successful remaining payment", orderId);
                }
                break;
            case "CANCELLED":
            case "EXPIRED":
                orderService.UpdateOrderStatus(order.getId(), OrderStatus.CANCELLED.name());
                logger.info("Order {} status updated to CANCELLED due to payment status: {}", orderId, status);
                break;
            case "PENDING":
                logger.info("Payment for order {} is still PENDING. No order status update performed.", orderId);
                break;
            default:
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
        Payments payment = paymentRepository.findOrdersId(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));

        PaymentLinkData canceledPayment = payOS.cancelPaymentLink(Long.parseLong(payment.getPayOsPaymentLinkId()), "Order cancelled");

        payment.setPaymentStatus(PaymentStatus.CANCELLED);
        paymentRepository.save(payment);
        orderService.UpdateOrderStatus(order.getId(), OrderStatus.CANCELLED.name());

        logger.info("Payment link canceled for order {}: payOsPaymentLinkId={}", orderId, payment.getPayOsPaymentLinkId());
        return canceledPayment;
    }

    @Override
    public void cancelPayment(String orderId) {
        logger.info("Canceling payment for order: {}", orderId);

        Payments payment = paymentRepository.findOrdersId(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
        payment.setPaymentStatus(PaymentStatus.CANCELLED);
        paymentRepository.save(payment);
        Orders order = payment.getOrders();
        orderService.UpdateOrderStatus(order.getId(), OrderStatus.CANCELLED.name());

        logger.info("Payment canceled and order {} status updated to CANCELLED", order.getId());
    }
}
