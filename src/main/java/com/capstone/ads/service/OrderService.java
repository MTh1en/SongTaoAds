package com.capstone.ads.service;

import com.capstone.ads.dto.order.OrderConfirmRequest;
import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.order.OrderUpdateInformationRequest;
import com.capstone.ads.model.Orders;
import com.capstone.ads.model.enums.OrderStatus;
import org.springframework.data.domain.Page;

public interface OrderService {
    OrderDTO createOrderByCustomDesign(String customDesignRequestId, String customerChoiceId);

    OrderDTO createOrderByAIDesign(String aiDesignId, String customerChoiceId);

    OrderDTO customerProvideAddress(String orderId, OrderUpdateInformationRequest request);

    OrderDTO saleNotifyEstimateDeliveryDate(String orderId, OrderConfirmRequest request);

    OrderDTO findOrderById(String orderId);

    Page<OrderDTO> findOrderByStatus(OrderStatus status, int page, int size);

    void hardDeleteOrder(String orderId);

    Page<OrderDTO> findOrderByUserId(String userId, int page, int size);

    //INTERNAL FUNCTION
    Orders getOrderById(String orderId);

    void updateOrderStatus(String orderId, OrderStatus status);
}
