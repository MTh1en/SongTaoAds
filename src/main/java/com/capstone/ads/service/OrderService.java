package com.capstone.ads.service;

import com.capstone.ads.dto.order.OrderConfirmRequest;
import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.order.OrderUpdateAddressRequest;
import com.capstone.ads.model.Orders;
import com.capstone.ads.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface OrderService {
    OrderDTO createOrderByCustomDesign(String customDesignRequestId);

    OrderDTO createOrderByAIDesign(String aiDesignId, String customerChoiceId);

    OrderDTO saleConfirmContractSigned(String orderId);

    OrderDTO customerProvideAddress(String orderId, OrderUpdateAddressRequest request);

    OrderDTO saleNotifyEstimateDeliveryDate(String orderId, OrderConfirmRequest request);

    OrderDTO staffUpdateOrderProducing(String orderId, MultipartFile draftImage);

    OrderDTO staffUpdateOrderProductionComplete(String orderId, MultipartFile productImage);

    OrderDTO staffUpdateOrderDelivering(String orderId, MultipartFile deliveryImage);

    OrderDTO staffUpdateOrderInstalled(String orderId, MultipartFile installedImage);

    OrderDTO findOrderById(String orderId);

    Page<OrderDTO> findOrderByStatus(OrderStatus status, int page, int size);

    void hardDeleteOrder(String orderId);

    Page<OrderDTO> findOrderByUserId(String userId, int page, int size);

    //INTERNAL FUNCTION
    Orders getOrderById(String orderId);

    void updateOrderStatus(String orderId, OrderStatus status);

    void updateOrderFromWebhookResult(Orders orders, boolean isDeposit);
}
