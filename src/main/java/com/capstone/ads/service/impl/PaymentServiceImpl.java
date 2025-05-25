package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.PaymentPolicy;
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
import com.capstone.ads.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.payos.PayOS;
import vn.payos.type.*;

import java.security.SecureRandom;

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

    @Value("${payos.return-url}")
    private String RETURN_URL;

    @Value("${payos.cancel-url}")
    private String CANCEL_URL;

    private final SecureRandom random = new SecureRandom();
    private final PaymentsRepository paymentRepository;
    private final OrdersRepository orderRepository;

    @Override
    public CheckoutResponseData createDepositPaymentLink(CreatePaymentRequest request) throws Exception {
        Orders order = validateOrder(request.getOrderId());
        Double depositAmount = calculateAmount(order.getTotalAmount(), PaymentPolicy.DEPOSIT_PERCENTAGE);
        order.setDepositAmount(depositAmount);
        return createPaymentLink(request, order, depositAmount, true);
    }

    @Override
    public CheckoutResponseData createRemainingPaymentLink(CreatePaymentRequest request) throws Exception {
        Orders order = validateOrder(request.getOrderId());
        if (order.getStatus() != OrderStatus.DEPOSITED) {
            log.error("Order {} is not in DEPOSITED status, current status: {}", request.getOrderId(), order.getStatus());
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        }
        Double remainingAmount = calculateAmount(order.getTotalAmount(), PaymentPolicy.REMAINING_PERCENTAGE);
        return createPaymentLink(request, order, remainingAmount, false);
    }

    @Override
    public void handlePayOsCallback(String orderId) {
        Orders orders = validateOrder(orderId);
        orders.getPayments().forEach(payment -> {
            {
                if (Boolean.FALSE.equals(payment.getIsDeposit())) {
                    orders.setStatus(OrderStatus.COMPLETED);
                } else {
                    orders.setStatus(OrderStatus.DEPOSITED);
                }
            }
        });
        orderRepository.save(orders);
    }

    @Override
    public WebhookData verifyPaymentWebhookData(Webhook Webhook) throws Exception {
        PayOS payOS = new PayOS(CLIENT_ID, API_KEY, CHECKSUM_KEY);
        return payOS.verifyPaymentWebhookData(Webhook);
    }

    @Override
    @Transactional
    public void updateOrderStatusByWebhookData(WebhookData webhookData) {
        Payments payment = paymentRepository.findByCode(webhookData.getOrderCode())
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
        if (webhookData.getCode().equals("00")) {
            payment.setStatus(PaymentStatus.SUCCESS);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
        }
        paymentRepository.saveAndFlush(payment);
        updateOrderStatus(payment);
    }

    private Orders validateOrder(String orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (order.getTotalAmount() == null || order.getTotalAmount() <= 0 || order.getUsers() == null) {
            log.error("Invalid order data for order {}: totalAmount or users is invalid", orderId);
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        return order;
    }

    private Double calculateAmount(Double totalAmount, Double percentage) {
        return totalAmount * percentage;
    }

    private CheckoutResponseData createPaymentLink(CreatePaymentRequest request, Orders order, Double amount, boolean isDeposit) throws Exception {
        PayOS payOS = new PayOS(CLIENT_ID, API_KEY, CHECKSUM_KEY);
        long paymentCode = generateOrderCode();

        long rounded = Math.round(amount);
        Integer payOsAmount = Math.toIntExact(rounded);

        // Create ItemData
        ItemData itemData = ItemData.builder()
                .name(String.format("%s Payment for Order of - %s",
                        isDeposit ? "Deposit" : "Remaining", order.getUsers().getFullName()))
                .price(payOsAmount)
                .quantity(1)
                .build();

        // Create PaymentData
        PaymentData paymentData = PaymentData.builder()
                .orderCode(paymentCode)
                .amount(payOsAmount)
                .description(request.getDescription())
                .returnUrl(RETURN_URL)
                .cancelUrl(CANCEL_URL + "/" + paymentCode)
                .item(itemData)
                .build();

        // Create and save Payment entity
        Payments payment = Payments.builder()
                .code(paymentCode)
                .totalAmount(payOsAmount)
                .method(PaymentMethod.PAYOS)
                .status(PaymentStatus.PENDING)
                .isDeposit(isDeposit)
                .orders(order)
                .build();
        paymentRepository.save(payment);

        // Create payment link
        CheckoutResponseData response = payOS.createPaymentLink(paymentData);

        log.info("Created {} payment link for order {}: payOsPaymentLinkId={}, paymentUrl={}",
                isDeposit ? "deposit" : "remaining", order.getId(), response.getPaymentLinkId(), response.getCheckoutUrl());

        return response;
    }

    private long generateOrderCode() {
        return Math.abs(random.nextLong() % 10000000000L);
    }

    private void updateOrderStatus(Payments payment) {
        Orders order = payment.getOrders();
        PaymentStatus paymentStatus = payment.getStatus();

        if (paymentStatus == PaymentStatus.SUCCESS) {
            order.setStatus(payment.getIsDeposit() ? OrderStatus.DEPOSITED : OrderStatus.COMPLETED);
        }
        orderRepository.save(order);
    }
}