package com.capstone.ads.utils;

import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.model.enums.OrderStatus;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Component
public class OrderStateValidator {
    private final Map<OrderStatus, Set<OrderStatus>> validTransitions;

    public OrderStateValidator() {
        this.validTransitions = new EnumMap<>(OrderStatus.class);
        initializeValidTransitions();
    }

    private void initializeValidTransitions() {
        validTransitions.put(OrderStatus.PENDING, Set.of(
                OrderStatus.CANCELLED,
                OrderStatus.DEPOSITED
        ));

        validTransitions.put(OrderStatus.DEPOSITED, Set.of(
                OrderStatus.CANCELLED,
                OrderStatus.PROCESSING
        ));

        validTransitions.put(OrderStatus.PROCESSING, Set.of(
                OrderStatus.CANCELLED,
                OrderStatus.IN_PRODUCTION
        ));

        validTransitions.put(OrderStatus.IN_PRODUCTION, Set.of(
                OrderStatus.CANCELLED,
                OrderStatus.DELIVERING
        ));

        validTransitions.put(OrderStatus.DELIVERING, Set.of(
                OrderStatus.CANCELLED,
                OrderStatus.COMPLETED
        ));

        validTransitions.put(OrderStatus.CANCELLED, Set.of());
        validTransitions.put(OrderStatus.COMPLETED, Set.of());
    }

    public boolean isValidTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        if (currentStatus == null || newStatus == null) {
            return false;
        }

        // Allow staying in the same state (if needed)
        if (currentStatus == newStatus) {
            return true;
        }
        return validTransitions.getOrDefault(currentStatus, Set.of())
                .contains(newStatus);
    }

    public void validateTransition(OrderStatus currentStatus,
                                   OrderStatus newStatus) {
        if (!isValidTransition(currentStatus, newStatus)) {
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS_TRANSITION);
        }
    }
}
