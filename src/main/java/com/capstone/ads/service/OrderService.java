package com.capstone.ads.service;

import com.capstone.ads.dto.order.OrderConfirmDTO;
import com.capstone.ads.dto.order.OrderCreateDTO;
import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.order.OrderUpdateDTO;

import java.util.List;

public interface OrderService {

    OrderDTO createOrder(OrderCreateDTO createDTO);
    OrderDTO getOrderById(String id);
    List<OrderDTO> getAllOrders();
    OrderDTO updateOrder(String id, OrderUpdateDTO updateDTO);
    void deleteOrder(String id);
    OrderDTO confirmOrder(String id, OrderConfirmDTO confirmDTO);
    void UpdateOrderStatus(String id, String status);
}
