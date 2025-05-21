package com.capstone.ads.service;

import com.capstone.ads.dto.order.OrderConfirmRequest;
import com.capstone.ads.dto.order.OrderCreateRequest;
import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.order.OrderUpdateRequest;

import java.util.List;

public interface OrderService {
    OrderDTO createOrder(OrderCreateRequest createDTO);

    OrderDTO getOrderById(String id);

    List<OrderDTO> getAllOrders();

    OrderDTO updateOrder(String id, OrderUpdateRequest updateDTO);

    void deleteOrder(String id);

    OrderDTO confirmOrder(String id, OrderConfirmRequest confirmDTO);

    void UpdateOrderStatus(String id, String status);

    OrderDTO getOrderByUserId(String id);
}
