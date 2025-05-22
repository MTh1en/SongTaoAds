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
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

import java.security.SecureRandom;
import java.time.LocalDateTime;

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
        double depositAmount = calculateAmount(order.getTotalAmount(), PaymentPolicy.DEPOSIT_PERCENTAGE);
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
        double remainingAmount = calculateAmount(order.getTotalAmount(), PaymentPolicy.REMAINING_PERCENTAGE);
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

    private Orders validateOrder(String orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (order.getTotalAmount() == null || order.getTotalAmount() <= 0 || order.getUsers() == null) {
            log.error("Invalid order data for order {}: totalAmount or users is invalid", orderId);
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        return order;
    }

    private double calculateAmount(double totalAmount, double percentage) {
        return Math.round(totalAmount * percentage * 100.0) / 100.0;
    }

    private CheckoutResponseData createPaymentLink(CreatePaymentRequest request, Orders order, double amount,
                                                   boolean isDeposit) throws Exception {
        PayOS payOS = new PayOS(CLIENT_ID, API_KEY, CHECKSUM_KEY);
        long orderCode = generateOrderCode();
        int amountInVnd = (int) Math.round(amount);

        // Create ItemData
        ItemData itemData = ItemData.builder()
                .name(String.format("%s Payment for Order of - %s",
                        isDeposit ? "Deposit" : "Remaining", order.getUsers().getFullName()))
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
                .totalAmount(amount)
                .paymentMethod(PaymentMethod.PAYOS)
                .paymentStatus(PaymentStatus.SUCCESS)
                .paymentDate(LocalDateTime.now())
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
        PaymentStatus paymentStatus = payment.getPaymentStatus();

        if (paymentStatus == PaymentStatus.SUCCESS) {
            order.setStatus(payment.getIsDeposit() ? OrderStatus.DEPOSITED : OrderStatus.COMPLETED);
        } else if (paymentStatus == PaymentStatus.CANCELLED || paymentStatus == PaymentStatus.EXPIRED) {
            order.setStatus(OrderStatus.CANCELLED);
        }
        orderRepository.save(order);
    }
}