package com.capstone.ads.service;

import com.capstone.ads.dto.order.*;
import com.capstone.ads.model.Orders;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.model.enums.OrderType;
import com.capstone.ads.model.enums.PaymentType;
import org.springframework.data.domain.Page;

public interface OrderService {
    OrderDTO createOrder(OrderCreateRequest request);

    OrderDTO saleRequestCustomerResignContract(String orderId);

    OrderDTO saleConfirmContractSigned(String orderId);

    OrderDTO customerProvideAddress(String orderId, OrderUpdateAddressRequest request);

    OrderDTO saleNotifyEstimateDeliveryDate(String orderId, OrderConfirmRequest request);

    OrderDTO findOrderById(String orderId);

    Page<OrderDTO> findOrderByStatus(OrderStatus status, int page, int size);

    Page<OrderDTO> findOrderByType(OrderType orderType, int page, int size);

    Page<OrderDTO> findOrderByStatusAndType(OrderStatus status, OrderType orderType, int page, int size);

    Page<OrderDTO> findCustomDesignOrderByAndStatus(OrderStatus status, int page, int size);

    Page<OrderDTO> findCustomDesignOrder(int page, int size);

    Page<OrderDTO> findAllOrders(int page, int size);

    void cancelOrder(String orderId);

    void hardDeleteOrder(String orderId);

    Page<OrderDTO> findOrderByUserId(String userId, int page, int size);

    //INTERNAL FUNCTION

    Orders getOrderById(String orderId);

    void updateOrderStatus(String orderId, OrderStatus status);

    boolean checkOrderNeedDepositDesign(String orderId);

    boolean checkOrderNeedFullyPaidDesign(String orderId);

    boolean checkOrderCustomDesignSubmittedDesign(String orderId);

    void updateOrderStatusAfterCustomDesignCompleted(String orderId);

    void updateAllAmount(Orders orders);

    void updateOrderFromWebhookResult(Orders orders, PaymentType paymentType);
}
