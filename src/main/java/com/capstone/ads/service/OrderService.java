package com.capstone.ads.service;

import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.order.OrderUpdateRequest;

import java.util.List;

public interface OrderService {
    OrderDTO createOrder(String customerChoiceId);

    OrderDTO getOrderById(String id);

    List<OrderDTO> getAllOrders();

    OrderDTO updateOrder(String id, OrderUpdateRequest updateDTO);

    void deleteOrder(String id);

    List<OrderDTO> getOrderByUserId(String id);
}
