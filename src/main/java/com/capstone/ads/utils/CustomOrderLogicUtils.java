package com.capstone.ads.utils;

import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.model.Orders;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.repository.internal.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomOrderLogicUtils {
    private final OrdersRepository orderRepository;

    public void updateOrderPendingContractAfterCustomDesignRequestCompleted(String customDesignRequestId) {
        Orders orders = orderRepository.findByCustomDesignRequests_Id(customDesignRequestId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        orders.setStatus(OrderStatus.PENDING_CONTRACT);
        orderRepository.save(orders);
    }
}
