package com.capstone.ads.service;

import com.capstone.ads.dto.order_detail.OrderDetailCreateRequest;
import com.capstone.ads.dto.order_detail.OrderDetailDTO;
import com.capstone.ads.model.OrderDetails;

import java.util.List;

public interface OrderDetailService {
    OrderDetailDTO createOrderDetail(String orderId, OrderDetailCreateRequest request);

    List<OrderDetailDTO> getOrderDetailsByOrderId(String orderId);

    void hardDeleteOrderDetail(String orderId);

    // INTERNAL FUNCTION //

    OrderDetails getOrderDetailById(String orderDetailId);
}
