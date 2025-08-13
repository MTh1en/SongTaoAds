package com.capstone.ads.service.impl;

import com.capstone.ads.dto.order_detail.OrderDetailCreateRequest;
import com.capstone.ads.dto.order_detail.OrderDetailDTO;
import com.capstone.ads.event.*;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.OrderDetailMapper;
import com.capstone.ads.model.*;
import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.repository.internal.OrderDetailsRepository;
import com.capstone.ads.service.*;
import com.capstone.ads.utils.DataConverter;
import io.micrometer.common.util.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailServiceImpl implements OrderDetailService {
    OrderService orderService;
    CustomerChoicesService customerChoicesService;
    CustomDesignRequestService customDesignRequestService;
    EditedDesignService editedDesignService;
    DataConverter dataConverter;
    OrderDetailsRepository orderDetailsRepository;
    OrderDetailMapper orderDetailMapper;
    ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public OrderDetailDTO createOrderDetail(String orderId, OrderDetailCreateRequest request) {
        Orders orders = orderService.getOrderById(orderId);
        CustomerChoices customerChoices = customerChoicesService.getCustomerChoiceById(request.getCustomerChoiceId());

        if (StringUtils.isNotBlank(request.getCustomDesignRequestId()) &&
                StringUtils.isNotBlank(request.getEditedDesignId())) {
            throw new AppException(ErrorCode.CONFLICTING_DESIGN_SOURCES);
        }

        OrderDetails orderDetails = orderDetailMapper.mapCreateRequestToEntity(request);
        orderDetails.setOrders(orders);
        orderDetails.setCreatedAt(LocalDateTime.now());
        orderDetails.setDetailConstructionAmount(customerChoices.getTotalAmount());
        orderDetails.setTotalDetailConstructionAmount(customerChoices.getTotalAmount() * request.getQuantity());
        orderDetails.setCustomerChoiceHistories(dataConverter.convertToHistory(customerChoices));

        if (StringUtils.isNotBlank(request.getCustomDesignRequestId())) {
            orderDetailsRepository.findByCustomDesignRequests_Id(request.getCustomDesignRequestId())
                    .ifPresent(existingOrderDetail -> {
                        throw new AppException(ErrorCode.ORDER_DETAIL_ALREADY_EXISTS);
                    });

            CustomDesignRequests customDesignRequests = customDesignRequestService.getCustomDesignRequestById(request.getCustomDesignRequestId());
            orderDetails.setCustomDesignRequests(customDesignRequests);
        }
        if (StringUtils.isNotBlank(request.getEditedDesignId())) {
            orderDetailsRepository.findByEditedDesigns_Id(request.getCustomDesignRequestId())
                    .ifPresent(existingOrderDetail -> {
                        throw new AppException(ErrorCode.ORDER_DETAIL_ALREADY_EXISTS);
                    });

            EditedDesigns editedDesigns = editedDesignService.getEditedDesignById(request.getEditedDesignId());
            orderDetails.setEditedDesigns(editedDesigns);
        }
        orderDetails = orderDetailsRepository.save(orderDetails);

        orders.getOrderDetails().add(orderDetails);
        orderService.updateAllAmount(orderDetails.getOrders());
        customerChoicesService.hardDeleteCustomerChoice(request.getCustomerChoiceId());
        return orderDetailMapper.toDTO(orderDetails);
    }

    @Override
    public List<OrderDetailDTO> getOrderDetailsByOrderId(String orderId) {
        return orderDetailsRepository.findByOrders_Id(orderId).stream()
                .map(orderDetailMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void hardDeleteOrderDetail(String orderDetailId) {
        OrderDetails orderDetails = getOrderDetailById(orderDetailId);
        Orders orders = orderService.getOrderById(orderDetails.getOrders().getId());

        orderDetailsRepository.deleteById(orderDetailId);
        orders.getOrderDetails().remove(orderDetails);
        orderService.updateAllAmount(orders);
    }

    // INTERNAL FUNCTION //

    @Override
    public OrderDetails getOrderDetailById(String orderDetailId) {
        return orderDetailsRepository.findById(orderDetailId).
                orElseThrow(() -> new AppException(ErrorCode.ORDER_DETAIL_NOT_FOUND));
    }

    // HANDLE EVENT //

    @Async("delegatingSecurityContextAsyncTaskExecutor")
    @EventListener
    @Transactional
    public void handlePriceProposalApprovedEvent(CustomDesignRequestPricingApprovedEvent event) {
        OrderDetails orderDetail = orderDetailsRepository.findByCustomDesignRequests_Id(event.getCustomDesignRequestId())
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOM_DESIGN_REQUEST_NOT_FOUND));
        String orderId = orderDetail.getOrders().getId();

        orderDetail.setDetailDesignAmount(event.getTotalPrice());
        orderDetail.setDetailDepositDesignAmount(event.getDepositAmount());
        orderDetail.setDetailRemainingDesignAmount(event.getRemainingAmount());
        orderDetail = orderDetailsRepository.save(orderDetail);

        orderService.updateAllAmount(orderDetail.getOrders());
        if (orderService.checkOrderNeedDepositDesign(orderId)) {
            orderService.updateOrderStatus(orderId, OrderStatus.NEED_DEPOSIT_DESIGN);
        }
    }

    @Async("delegatingSecurityContextAsyncTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDemoDesignApprovedEvent(CustomDesignRequestDemoSubmittedEvent event) {
        OrderDetails orderDetail = orderDetailsRepository.findByCustomDesignRequests_Id(event.getCustomDesignRequestId())
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOM_DESIGN_REQUEST_NOT_FOUND));
        String orderId = orderDetail.getOrders().getId();

        if (orderService.checkOrderNeedFullyPaidDesign(orderId)) {
            orderService.updateOrderStatus(orderId, OrderStatus.NEED_FULLY_PAID_DESIGN);
        }
    }

    @Async("delegatingSecurityContextAsyncTaskExecutor")
    @EventListener
    @Transactional
    public void handleCustomDesignRequestCompletedEvent(CustomDesignRequestCompletedEvent event) {
        log.info("Custom design request completed: {}", event.getCustomDesignRequestId());
        OrderDetails orderDetail = orderDetailsRepository.findByCustomDesignRequests_Id(event.getCustomDesignRequestId())
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOM_DESIGN_REQUEST_NOT_FOUND));
        String orderId = orderDetail.getOrders().getId();

        if (orderService.checkOrderCustomDesignSubmittedDesign(orderId)) {
            orderService.updateOrderStatusAfterCustomDesignCompleted(orderId);
        }
    }

    @Async("delegatingSecurityContextAsyncTaskExecutor")
    @EventListener
    @Transactional
    public void handleOrderCancel(OrderCancelEvent event) {
        Orders orders = orderService.getOrderById(event.getOrderId());

        orders.getOrderDetails().forEach(orderDetail -> {
            eventPublisher.publishEvent(new CustomDesignRequestChangeStatusEvent(
                    this,
                    orderDetail.getCustomDesignRequests().getId(),
                    CustomDesignRequestStatus.CANCELLED
            ));
        });
    }
}
