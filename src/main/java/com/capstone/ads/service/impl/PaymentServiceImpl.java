package com.capstone.ads.service.impl;

import com.capstone.ads.dto.payment.PaymentDTO;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.PaymentMapper;
import com.capstone.ads.model.Orders;
import com.capstone.ads.model.Payments;
import com.capstone.ads.model.enums.*;
import com.capstone.ads.repository.internal.PaymentsRepository;
import com.capstone.ads.service.OrderService;
import com.capstone.ads.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.payos.PayOS;
import vn.payos.type.*;

import java.security.SecureRandom;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentServiceImpl implements PaymentService {
    @NonFinal
    @Value("${payos.client-id}")
    private String CLIENT_ID;

    @NonFinal
    @Value("${payos.api-key}")
    private String API_KEY;

    @NonFinal
    @Value("${payos.checksum-key}")
    private String CHECKSUM_KEY;

    @NonFinal
    @Value("${app.base.url}")
    private String BASE_URL;

    SecureRandom random = new SecureRandom();
    PaymentsRepository paymentRepository;
    PaymentMapper paymentMapper;
    OrderService orderService;

    @Override
    public CheckoutResponseData createConstructionDepositPaymentLink(String orderId) throws Exception {
        Orders order = orderService.getOrderById(orderId);
        return createPaymentLinkForOrder(order, true);
    }

    @Override
    public CheckoutResponseData createConstructionRemainingPaymentLink(String orderId) throws Exception {
        Orders order = orderService.getOrderById(orderId);
        return createPaymentLinkForOrder(order, false);
    }

    @Override
    public CheckoutResponseData createCustomDesignFullDepositPaymentLink(String orderId) throws Exception {
        Orders order = orderService.getOrderById(orderId);
        return createPaymentLinkForCustomDesign(order, true);
    }

    @Override
    public CheckoutResponseData createCustomDesignFullRemainingPaymentLink(String orderId) throws Exception {
        Orders order = orderService.getOrderById(orderId);
        return createPaymentLinkForCustomDesign(order, false);
    }

    @Override
    public CheckoutResponseData createFullOrderPaymentLink(String orderId) throws Exception {
        Orders order = orderService.getOrderById(orderId);
        return createPaymentLinkForFullPayment(order);
    }

    @Override
    public WebhookData verifyPaymentWebhookData(Webhook Webhook) throws Exception {
        PayOS payOS = new PayOS(CLIENT_ID, API_KEY, CHECKSUM_KEY);
        return payOS.verifyPaymentWebhookData(Webhook);
    }

    @Override
    @Transactional
    public void updateStatusByWebhookData(Webhook Webhook) {
        Payments payment = paymentRepository.findByCode(Webhook.getData().getOrderCode())
                .orElse(null);
        if (Objects.nonNull(payment)) {
            if (Webhook.getSuccess()) {
                payment.setStatus(PaymentStatus.SUCCESS);
                orderService.updateOrderFromWebhookResult(payment.getOrders(), payment.getType());
            } else {
                payment.setStatus(PaymentStatus.FAILED);
            }
            paymentRepository.save(payment);
        }
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

    @Override
    public Page<PaymentDTO> findPaymentByOrderId(String orderId, int page, int size) {
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return paymentRepository.findByOrders_Id(orderId, pageable)
                .map(paymentMapper::toDTO);
    }

    @Override
    public Page<PaymentDTO> findPaymentByUserId(String userId, int page, int size) {
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return paymentRepository.findByOrders_Users_Id(userId, pageable)
                .map(paymentMapper::toDTO);
    }

    //INTERNAL FUNCTION //
    private CheckoutResponseData createPaymentLinkForOrder(Orders order, boolean isDeposit) throws Exception {
        long paymentCode = generateOrderCode();
        Long amount;
        PaymentType paymentType;
        if (isDeposit) {
            amount = order.getDepositConstructionAmount();
            paymentType = PaymentType.DEPOSIT_CONSTRUCTION;
        } else {
            amount = order.getRemainingConstructionAmount();
            paymentType = PaymentType.REMAINING_CONSTRUCTION;
        }

        Payments payment = Payments.builder()
                .code(paymentCode)
                .amount(amount)
                .method(PaymentMethod.PAYOS)
                .status(PaymentStatus.PENDING)
                .type(paymentType)
                .orders(order)
                .build();
        paymentRepository.save(payment);

        return createPaymentLinkFromPayOS(amount.intValue(), paymentCode);
    }

    private CheckoutResponseData createPaymentLinkForCustomDesign(Orders order, boolean isDeposit) throws Exception {
        long paymentCode = generateOrderCode();
        long amount;
        PaymentType paymentType;
        if (isDeposit) {
            amount = order.getDepositDesignAmount();
            paymentType = PaymentType.DEPOSIT_DESIGN;
        } else {
            amount = order.getRemainingDesignAmount();
            paymentType = PaymentType.REMAINING_DESIGN;
        }

        Payments payment = Payments.builder()
                .code(paymentCode)
                .amount(amount)
                .method(PaymentMethod.PAYOS)
                .status(PaymentStatus.PENDING)
                .type(paymentType)
                .orders(order)
                .build();
        paymentRepository.save(payment);

        return createPaymentLinkFromPayOS((int) amount, paymentCode);
    }

    private CheckoutResponseData createPaymentLinkForFullPayment(Orders orders) throws Exception {
        long paymentCode = generateOrderCode();
        Long amount;

        if (orders.getOrderType().equals(OrderType.AI_DESIGN)) {
            amount = orders.getTotalConstructionAmount();
        } else {
            amount = orders.getTotalDesignAmount() + orders.getTotalConstructionAmount();
        }

        Payments payment = Payments.builder()
                .code(paymentCode)
                .amount(amount)
                .method(PaymentMethod.PAYOS)
                .status(PaymentStatus.PENDING)
                .type(PaymentType.FULLY_PAID)
                .orders(orders)
                .build();

        paymentRepository.save(payment);
        return createPaymentLinkFromPayOS(amount.intValue(), paymentCode);
    }
    // INTERNAL FUNCTION //

    private CheckoutResponseData createPaymentLinkFromPayOS(Integer amount, Long paymentCode) throws Exception {
        PayOS payOS = new PayOS(CLIENT_ID, API_KEY, CHECKSUM_KEY);

        long expiredInSeconds = (System.currentTimeMillis() / 1000) + (60 * 60);

        PaymentData paymentData = PaymentData.builder()
                .orderCode(paymentCode)
                .description("SongTaoAds")
                .amount(amount)
                .expiredAt(expiredInSeconds)
                .returnUrl(BASE_URL + "/api/payments/success")
                .cancelUrl(BASE_URL + "/api/payments/fail/" + paymentCode)
                .build();
        return payOS.createPaymentLink(paymentData);
    }

    private long generateOrderCode() {
        return Math.abs(random.nextLong() % 10000000000L);
    }
}