package com.capstone.ads.utils;

import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.model.enums.OrderStatus;
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
