package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.NotificationMessage;
import com.capstone.ads.constaint.PaymentPolicy;
import com.capstone.ads.constaint.PredefinedRole;
import com.capstone.ads.dto.order.*;
import com.capstone.ads.event.*;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.OrdersMapper;
import com.capstone.ads.model.*;
import com.capstone.ads.model.enums.*;
import com.capstone.ads.repository.internal.OrdersRepository;
import com.capstone.ads.service.*;
import com.capstone.ads.utils.KeyGenerator;
import com.capstone.ads.validator.ContractStateValidator;
import com.capstone.ads.validator.OrderStateValidator;
import com.capstone.ads.utils.SecurityContextUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {
    OrdersRepository orderRepository;
    ContractorService contractorService;
    OrdersMapper orderMapper;
    SecurityContextUtils securityContextUtils;
    OrderStateValidator orderStateValidator;
    ContractStateValidator contractStateValidator;
    ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public OrderDTO createOrder(OrderCreateRequest request) {
        Users users = securityContextUtils.getCurrentUser();
        Orders orders = orderMapper.mapCreateRequestToEntity(request);
        orders.setOrderCode(KeyGenerator.generateOrderCode());
        orders.setUsers(users);
        orders.setStatus(
                (request.getOrderType().equals(OrderType.AI_DESIGN))
                        ? OrderStatus.PENDING_CONTRACT
                        : OrderStatus.PENDING_DESIGN
        );
        orders = orderRepository.save(orders);

        return orderMapper.toDTO(orders);
    }

    @Override
    @Transactional
    public OrderDTO saleRequestCustomerResignContract(String orderId) {
        Orders orders = getOrderById(orderId);
        String userId = securityContextUtils.getCurrentUser().getId();

        orderStateValidator.validateTransition(orders.getStatus(), OrderStatus.CONTRACT_RESIGNED);
        contractStateValidator.validateTransition(orders.getContract().getStatus(), ContractStatus.NEED_RESIGNED);

        orders.setStatus(OrderStatus.CONTRACT_RESIGNED);
        orders.getContract().setStatus(ContractStatus.NEED_RESIGNED);
        orderRepository.save(orders);

        eventPublisher.publishEvent(new OrderStatusChangedEvent(
                this,
                orderId,
                OrderStatus.CONTRACT_RESIGNED,
                userId,
                null
        ));

        eventPublisher.publishEvent(new UserNotificationEvent(
                this,
                orders.getUsers().getId(),
                String.format(NotificationMessage.DEFAULT, orders.getOrderCode(), OrderStatus.CONTRACT_RESIGNED.getMessage())
        ));

        return orderMapper.toDTO(orders);
    }

    @Override
    @Transactional
    public OrderDTO saleConfirmContractSigned(String orderId) {
        Orders orders = getOrderById(orderId);
        Contract contract = orders.getContract();
        String userId = securityContextUtils.getCurrentUser().getId();

        orderStateValidator.validateTransition(orders.getStatus(), OrderStatus.CONTRACT_CONFIRMED);

        long depositAmount = (long) ((!contract.getDepositPercentChanged().equals(PaymentPolicy.DEPOSIT_PERCENT))
                ? orders.getTotalConstructionAmount() * ((double) contract.getDepositPercentChanged() / 100)
                : orders.getTotalConstructionAmount() * ((double) PaymentPolicy.DEPOSIT_PERCENT / 100));
        long remainingAmount = orders.getTotalConstructionAmount() - depositAmount;

        orders.setStatus(OrderStatus.CONTRACT_CONFIRMED);
        orders.getContract().setStatus(ContractStatus.CONFIRMED);

        orders.setDepositConstructionAmount(depositAmount);
        orders.setRemainingConstructionAmount(remainingAmount);

        orders.setTotalOrderDepositAmount(orders.getDepositDesignAmount() == 0
                ? depositAmount
                : orders.getDepositDesignAmount() + depositAmount
        );
        orders.setTotalOrderRemainingAmount(orders.getRemainingDesignAmount() == 0
                ? remainingAmount
                : orders.getRemainingDesignAmount() + remainingAmount
        );

        orderRepository.save(orders);

        eventPublisher.publishEvent(new OrderStatusChangedEvent(
                this,
                orderId,
                OrderStatus.CONTRACT_CONFIRMED,
                userId,
                null
        ));

        eventPublisher.publishEvent(new UserNotificationEvent(
                this,
                orders.getUsers().getId(),
                String.format(NotificationMessage.DEFAULT, orders.getOrderCode(), OrderStatus.CONTRACT_CONFIRMED.getMessage())
        ));

        return orderMapper.toDTO(orders);
    }

    @Override
    @Transactional
    public OrderDTO customerProvideAddress(String orderId, OrderUpdateAddressRequest request) {
        Orders orders = getOrderById(orderId);

        orderMapper.updateEntityFromUpdateInformationRequest(request, orders);
        orders = orderRepository.save(orders);

        return orderMapper.toDTO(orders);
    }

    @Override
    @Transactional
    public OrderDTO saleNotifyEstimateDeliveryDate(String orderId, OrderConfirmRequest request) {
        Orders orders = getOrderById(orderId);
        Contractors contractors = contractorService.getContractorById(request.getContractorId());
        String userId = securityContextUtils.getCurrentUser().getId();

        orderStateValidator.validateTransition(orders.getStatus(), OrderStatus.IN_PROGRESS);

        orderMapper.updateEntityFromConfirmRequest(request, orders);
        orders.setStatus(OrderStatus.IN_PROGRESS);
        orders.setContractors(contractors);
        orders = orderRepository.save(orders);

        eventPublisher.publishEvent(new OrderStatusChangedEvent(
                this,
                orderId,
                OrderStatus.IN_PROGRESS,
                userId,
                null
        ));

        eventPublisher.publishEvent(new UserNotificationEvent(
                this,
                orders.getUsers().getId(),
                String.format(NotificationMessage.DEFAULT, orders.getOrderCode(), OrderStatus.IN_PROGRESS.getMessage())
        ));

        eventPublisher.publishEvent(new RoleNotificationEvent(
                this,
                PredefinedRole.STAFF_ROLE,
                String.format(NotificationMessage.ORDER_IN_PROGRESS, orders.getOrderCode())
        ));

        return orderMapper.toDTO(orders);
    }

    @Override
    public OrderDTO findOrderById(String orderId) {
        Orders orders = getOrderById(orderId);
        return orderMapper.toDTO(orders);
    }

    @Override
    public Page<OrderDTO> findOrders(OrderStatus orderStatus, OrderType orderType, int page, int size) {
        Sort sort = Sort.by("updatedAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        if (orderStatus != null && orderType != null) {
            return orderRepository.findByStatusAndOrderType(orderStatus, orderType, pageable)
                    .map(orderMapper::toDTO);
        } else if (orderStatus != null) {
            return orderRepository.findByStatus(orderStatus, pageable)
                    .map(orderMapper::toDTO);
        } else if (orderType != null) {
            return orderRepository.findByOrderType(orderType, pageable)
                    .map(orderMapper::toDTO);
        } else {
            return orderRepository.findAll(pageable)
                    .map(orderMapper::toDTO);
        }
    }

    @Override
    public Page<OrderDTO> findCustomDesignOrder(OrderStatus status, int page, int size) {
        List<OrderType> orderTypes = Arrays.asList(
                OrderType.CUSTOM_DESIGN_WITH_CONSTRUCTION,
                OrderType.CUSTOM_DESIGN_WITHOUT_CONSTRUCTION
        );

        Sort sort = Sort.by("updatedAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        if (status != null) {
            return orderRepository.findByStatusAndOrderTypeIn(status, orderTypes, pageable)
                    .map(orderMapper::toDTO);
        } else {
            return orderRepository.findByOrderTypeIn(orderTypes, pageable)
                    .map(orderMapper::toDTO);
        }
    }

    @Override
    @Transactional
    public void cancelOrder(String orderId) {
        Orders orders = getOrderById(orderId);
        orderStateValidator.validateTransition(orders.getStatus(), OrderStatus.CANCELLED);

        if (orderStateValidator.isCancelOrderDesignStatus(orders.getStatus())) {
            eventPublisher.publishEvent(new OrderCancelEvent(
                    this,
                    orderId
            ));
        }

        orders.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(orders);
    }

    @Override
    @Transactional
    public void hardDeleteOrder(String orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        orderRepository.deleteById(orderId);
    }

    @Override
    public Page<OrderDTO> findOrderByUserId(String userId, int page, int size) {
        Sort sort = Sort.by("updatedAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return orderRepository.findByUsers_Id(userId, pageable).map(orderMapper::toDTO);
    }

    @Override
    public Page<OrderDTO> searchAiOrders(String query, int page, int size) {
        List<OrderType> orderTypes = List.of(
                OrderType.AI_DESIGN
        );

        Sort sort = Sort.by("updatedAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        return orderRepository.searchSaleOrder(query, query, query, orderTypes, pageable)
                .map(orderMapper::toDTO);
    }

    @Override
    public Page<OrderDTO> searchCustomOrders(String query, int page, int size) {
        List<OrderType> orderTypes = Arrays.asList(
                OrderType.CUSTOM_DESIGN_WITH_CONSTRUCTION,
                OrderType.CUSTOM_DESIGN_WITHOUT_CONSTRUCTION
        );

        Sort sort = Sort.by("updatedAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        return orderRepository.searchSaleOrder(query, query, query, orderTypes, pageable)
                .map(orderMapper::toDTO);
    }

    @Override
    public Page<OrderDTO> searchCustomerOrders(String orderCode, int page, int size) {
        Users currentUsers = securityContextUtils.getCurrentUser();
        Sort sort = Sort.by("updatedAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        return orderRepository.findByOrderCodeContainsIgnoreCaseAndUsers(orderCode, currentUsers, pageable)
                .map(orderMapper::toDTO);
    }

    @Override
    public Page<OrderDTO> searchProductionOrders(String query, int page, int size) {
        List<OrderStatus> productionStatus = Arrays.asList(
                OrderStatus.IN_PROGRESS,
                OrderStatus.PRODUCING,
                OrderStatus.PRODUCTION_COMPLETED,
                OrderStatus.DELIVERING,
                OrderStatus.INSTALLED
                );

        Sort sort = Sort.by("updatedAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        return orderRepository.searchProductionOrder(query, query, query, productionStatus, pageable)
                .map(orderMapper::toDTO);
    }

    //INTERNAL FUNCTION//

    @Override
    public Orders getOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
    }

    @Override
    @Transactional
    public void updateOrderStatus(String orderId, OrderStatus status) {
        Orders orders = getOrderById(orderId);
        String userId = securityContextUtils.getCurrentUser().getId();

        orderStateValidator.validateTransition(orders.getStatus(), status);
        orders.setStatus(status);
        orderRepository.save(orders);

        eventPublisher.publishEvent(new OrderStatusChangedEvent(
                this,
                orderId,
                status,
                userId,
                null
        ));

        eventPublisher.publishEvent(new UserNotificationEvent(
                this,
                orders.getUsers().getId(),
                String.format(NotificationMessage.DEFAULT, orders.getOrderCode(), status.getMessage())
        ));
    }

    @Override
    public boolean checkOrderNeedDepositDesign(String orderId) {
        Orders orders = getOrderById(orderId);
        return orders.getOrderDetails().stream()
                .allMatch(orderDetails ->
                        orderDetails.getCustomDesignRequests().getStatus().equals(CustomDesignRequestStatus.APPROVED_PRICING));
    }

    @Override
    public boolean checkOrderNeedFullyPaidDesign(String orderId) {
        Orders orders = getOrderById(orderId);
        return orders.getOrderDetails().stream()
                .allMatch(orderDetails ->
                        orderDetails.getCustomDesignRequests().getStatus().equals(CustomDesignRequestStatus.WAITING_FULL_PAYMENT));
    }

    @Override
    public boolean checkOrderCustomDesignSubmittedDesign(String orderId) {
        Orders orders = getOrderById(orderId);
        return orders.getOrderDetails().stream()
                .allMatch(orderDetails ->
                        orderDetails.getCustomDesignRequests().getStatus().equals(CustomDesignRequestStatus.COMPLETED)
                );
    }

    @Override
    @Transactional
    public void updateOrderStatusAfterCustomDesignCompleted(String orderId) {
        Orders orders = getOrderById(orderId);
        if (orders.getOrderType().equals(OrderType.CUSTOM_DESIGN_WITH_CONSTRUCTION)) {
            orders.setStatus(OrderStatus.PENDING_CONTRACT);
        } else {
            orders.setStatus(OrderStatus.DESIGN_COMPLETED);
        }
        orderRepository.save(orders);
    }

    @Override
    @Transactional
    public void updateOrderFromWebhookResult(Orders orders, PaymentType paymentType) {
        OrderStatus status = null;
        if (paymentType.equals(PaymentType.DEPOSIT_CONSTRUCTION)) {
            orders.setStatus(OrderStatus.DEPOSITED);

            eventPublisher.publishEvent(new RoleNotificationEvent(
                    this,
                    PredefinedRole.SALE_ROLE,
                    String.format(NotificationMessage.ORDER_DEPOSITED, orders.getOrderCode())
            ));

        } else if (paymentType.equals(PaymentType.REMAINING_CONSTRUCTION)) {
            orders.setStatus(OrderStatus.ORDER_COMPLETED);
        } else if (paymentType.equals(PaymentType.DEPOSIT_DESIGN)) {
            orders.getOrderDetails().stream()
                    .filter(orderDetails -> orderDetails.getCustomDesignRequests().getStatus().equals(CustomDesignRequestStatus.APPROVED_PRICING))
                    .forEach(orderDetail ->
                            eventPublisher.publishEvent(new CustomDesignPaymentEvent(
                                    this,
                                    orderDetail.getCustomDesignRequests().getId(),
                                    true
                            ))
                    );
            status = OrderStatus.DEPOSITED_DESIGN;
            orders.setStatus(status);
        } else if (paymentType.equals(PaymentType.REMAINING_DESIGN)) {
            orders.getOrderDetails().stream()
                    .filter(orderDetails -> orderDetails.getCustomDesignRequests().getStatus().equals(CustomDesignRequestStatus.WAITING_FULL_PAYMENT))
                    .forEach(orderDetail ->
                            eventPublisher.publishEvent(new CustomDesignPaymentEvent(
                                    this,
                                    orderDetail.getCustomDesignRequests().getId(),
                                    false
                            ))
                    );
            status = OrderStatus.WAITING_FINAL_DESIGN;
            orders.setStatus(status);
        }
        orderRepository.save(orders);

        eventPublisher.publishEvent(new OrderStatusChangedEvent(
                this,
                orders.getId(),
                status,
                "WEBHOOK",
                null
        ));
    }

    @Override
    public void updateAllAmount(Orders orders) {
        long totalConstructionAmount = 0L;
        long depositConstructionAmount = 0L;
        long totalDesignAmount = 0L;
        long depositDesignAmount = 0L;
        long remainingDesignAmount = 0L;

        List<OrderDetails> orderDetails = Optional.ofNullable(orders.getOrderDetails()).orElse(Collections.emptyList());
        totalConstructionAmount += orderDetails.stream()
                .filter(detail -> detail.getDetailConstructionAmount() != null)
                .mapToLong(OrderDetails::getTotalDetailConstructionAmount)
                .sum();
        depositConstructionAmount += orderDetails.stream()
                .filter(detail -> detail.getDetailConstructionAmount() != null)
                .mapToLong(detail -> detail.getTotalDetailConstructionAmount() * PaymentPolicy.DEPOSIT_PERCENT / 100)
                .sum();
        totalDesignAmount += orderDetails.stream()
                .filter(detail -> detail.getDetailDesignAmount() != null)
                .mapToLong(OrderDetails::getDetailDesignAmount)
                .sum();
        depositDesignAmount += orderDetails.stream()
                .filter(detail -> detail.getDetailDepositDesignAmount() != null)
                .mapToLong(OrderDetails::getDetailDepositDesignAmount)
                .sum();
        remainingDesignAmount += orderDetails.stream()
                .filter(detail -> detail.getDetailDepositDesignAmount() != null)
                .mapToLong(OrderDetails::getDetailRemainingDesignAmount)
                .sum();

        orders.setTotalConstructionAmount(totalConstructionAmount);
        orders.setDepositConstructionAmount(depositConstructionAmount);
        orders.setRemainingConstructionAmount(totalConstructionAmount - depositConstructionAmount);

        orders.setTotalDesignAmount(totalDesignAmount);
        orders.setDepositDesignAmount(depositDesignAmount);
        orders.setRemainingDesignAmount(remainingDesignAmount);

        orders.setTotalOrderAmount(totalConstructionAmount + totalDesignAmount);
        orders.setTotalOrderDepositAmount(depositConstructionAmount + depositDesignAmount);
        orders.setTotalOrderRemainingAmount(
                (totalConstructionAmount - depositConstructionAmount) + remainingDesignAmount
        );

        orderRepository.save(orders);
    }
}
