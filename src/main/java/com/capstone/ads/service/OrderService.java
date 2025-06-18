package com.capstone.ads.service;

import com.capstone.ads.dto.order.OrderConfirmRequest;
import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.order.OrderUpdateInformationRequest;
import com.capstone.ads.model.enums.OrderStatus;
import org.springframework.data.domain.Page;

public interface OrderService {
    OrderDTO createOrderByCustomDesign(String customDesignRequestId, String customerChoiceId);

    OrderDTO createOrderByAIDesign(String aiDesignId, String customerChoiceId);

    OrderDTO findOrderById(String orderId);

    Page<OrderDTO> findOrderByStatus(OrderStatus status, int page, int size);

    OrderDTO customerUpdateOrderInformation(String orderId, OrderUpdateInformationRequest request);

    OrderDTO saleConfirmOrder(String orderId, OrderConfirmRequest request);

    void hardDeleteOrder(String orderId);

    OrderDTO changeOrderStatus(String orderId, OrderStatus orderStatus);

    Page<OrderDTO> findOrderByUserId(String userId, int page, int size);
}
