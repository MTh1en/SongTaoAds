package com.capstone.ads.service.impl;

import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.order.OrderUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.OrdersMapper;
import com.capstone.ads.model.Orders;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.repository.internal.CustomerChoicesRepository;
import com.capstone.ads.repository.internal.OrdersRepository;
import com.capstone.ads.service.OrderService;
import com.capstone.ads.utils.CustomerChoiceHistoriesConverter;
import com.capstone.ads.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final CustomerChoicesRepository customerChoicesRepository;
    private final OrdersRepository orderRepository;
    private final OrdersMapper orderMapper;
    private final SecurityContextUtils securityContextUtils;
    private final CustomerChoiceHistoriesConverter customerChoiceHistoriesConverter;

    @Override
    @Transactional
    public OrderDTO createOrder(String customerChoiceId) {
        var customerChoice = customerChoicesRepository.findById(customerChoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_NOT_FOUND));
        var user = securityContextUtils.getCurrentUser();
        Orders orders = Orders.builder()
                .orderDate(LocalDateTime.now())
                .totalAmount(customerChoice.getTotalAmount())
                .users(user)
                .customerChoiceHistories(customerChoiceHistoriesConverter.convertToHistory(customerChoice))
                .status(OrderStatus.PENDING)
                .build();
        orderRepository.save(orders);
        customerChoicesRepository.deleteById(customerChoiceId);
        return orderMapper.toDTO(orders);
    }

    @Override
    public OrderDTO getOrderById(String id) {
        Orders orders = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        return orderMapper.toDTO(orders);
    }

    @Override
    public List<OrderDTO> getOrderByUserId(String id) {
        return orderRepository.findByUsers_Id(id)
                .stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO updateOrder(String orderId, OrderUpdateRequest updateDTO) {
        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        orderMapper.updateEntityFromDTO(updateDTO, orders);
        orders = orderRepository.save(orders);
        return orderMapper.toDTO(orders);
    }

    @Override
    public void deleteOrder(String id) {
        if (!orderRepository.existsById(id)) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        orderRepository.deleteById(id);
    }
}
