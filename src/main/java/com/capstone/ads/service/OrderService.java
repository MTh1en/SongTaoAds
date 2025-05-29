package com.capstone.ads.service;

import com.capstone.ads.dto.order.OrderConfirmRequest;
import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.order.OrderUpdateInformationRequest;
import com.capstone.ads.model.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderDTO createOrderByCustomDesign(String customDesignId);

    OrderDTO createOrderByAIDesign(String customerChoiceId, String aiDesignId);

    OrderDTO findOrderById(String orderId);

    List<OrderDTO> findOrderByStatus(OrderStatus status);

    OrderDTO customerUpdateOrderInformation(String orderId, OrderUpdateInformationRequest request);

    OrderDTO saleConfirmOrder(String orderId, OrderConfirmRequest request);

    void hardDeleteOrder(String orderId);

    OrderDTO changeOrderStatus(String orderId, OrderStatus orderStatus);

    List<OrderDTO> findOrderByUserId(String userId);
}
