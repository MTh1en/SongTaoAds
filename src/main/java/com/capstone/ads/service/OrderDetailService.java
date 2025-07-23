package com.capstone.ads.service;

import com.capstone.ads.dto.order_detail.OrderDetailCreateRequest;
import com.capstone.ads.dto.order_detail.OrderDetailDTO;
import com.capstone.ads.model.OrderDetails;

public interface OrderDetailService {
    OrderDetailDTO createOrderDetail(String orderId, OrderDetailCreateRequest request);

    // INTERNAL FUNCTION //

    OrderDetails getOrderDetailById(String orderDetailId);
}
