package com.capstone.ads.service.impl;

import com.capstone.ads.dto.order.OrderConfirmDTO;
import com.capstone.ads.dto.order.OrderCreateDTO;
import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.order.OrderUpdateDTO;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.OrdersMapper;
import com.capstone.ads.model.AIDesigns;
import com.capstone.ads.model.Orders;

import com.capstone.ads.model.Payments;
import com.capstone.ads.model.Users;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.repository.internal.AIDesignsRepository;
import com.capstone.ads.repository.internal.OrdersRepository;
import com.capstone.ads.repository.internal.UsersRepository;
import com.capstone.ads.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.payos.type.PaymentLinkData;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AIDesignsRepository aiDesignsRepository;
    @Autowired
    private OrdersRepository orderRepository;

    @Autowired
    private OrdersMapper orderMapper;

    @Override
    public OrderDTO createOrder(OrderCreateDTO createDTO) {
        Users user = usersRepository.findById(createDTO.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));



        if (createDTO.getAiDesignId() != null) {
            AIDesigns aiDesign = aiDesignsRepository.findById(createDTO.getAiDesignId())
                    .orElseThrow(() -> new AppException(ErrorCode.AI_DESIGN_NOT_FOUND));

        }
        if (createDTO.getAiDesignId() != null) {
            AIDesigns aiDesign = aiDesignsRepository.findById(createDTO.getAiDesignId())
                    .orElseThrow(() -> new AppException(ErrorCode.AI_DESIGN_NOT_FOUND));
            if (orderRepository.existsByAiDesignsId(createDTO.getAiDesignId())) {
                throw new AppException(ErrorCode.AI_DESIGN_ALREADY_USED);
            }

        }
        Orders orders = orderMapper.toEntity(createDTO);
        orders.setStatus(OrderStatus.PENDING);
        orders.setDeliveryDate(null);
        orders = orderRepository.save(orders);
        return orderMapper.toDTO(orders);
    }

    @Override
    public OrderDTO getOrderById(String id) {
        Orders orders = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        return orderMapper.toDTO(orders);
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO updateOrder(String id, OrderUpdateDTO updateDTO) {
        Orders orders = orderRepository.findById(id)
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
    @Override
    public OrderDTO confirmOrder(String id, OrderConfirmDTO confirmDTO) {
        Orders orders = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        orders.setStatus(OrderStatus.PROCESSING); // Change status to PROCESSING
        orders.setDeliveryDate(confirmDTO.getDeliveryDate()); // Update deliveryDate
        orders = orderRepository.save(orders);
        return orderMapper.toDTO(orders);
    }

    @Override
    public void UpdateOrderStatus(String id, String status) {
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        // Validate and convert the status string to OrderStatus enum
        OrderStatus orderStatus;
        try {
            orderStatus = OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        }

        // Update the order status
        order.setStatus(orderStatus);
        orderRepository.save(order);
    }
}
