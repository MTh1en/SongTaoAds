package com.capstone.ads.validator;

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
        validTransitions.put(OrderStatus.PENDING_DESIGN, Set.of(
                OrderStatus.CANCELLED,
                OrderStatus.NEED_DEPOSIT_DESIGN
        ));

        validTransitions.put(OrderStatus.NEED_DEPOSIT_DESIGN, Set.of(
                OrderStatus.CANCELLED,
                OrderStatus.DEPOSITED_DESIGN
        ));

        validTransitions.put(OrderStatus.DEPOSITED_DESIGN, Set.of(
                OrderStatus.CANCELLED,
                OrderStatus.NEED_FULLY_PAID_DESIGN,
                OrderStatus.WAITING_FINAL_DESIGN
        ));

        validTransitions.put(OrderStatus.NEED_FULLY_PAID_DESIGN, Set.of(
                OrderStatus.CANCELLED,
                OrderStatus.WAITING_FINAL_DESIGN
        ));

        validTransitions.put(OrderStatus.WAITING_FINAL_DESIGN, Set.of(
                OrderStatus.CANCELLED,
                OrderStatus.PENDING_CONTRACT
        ));

        validTransitions.put(OrderStatus.PENDING_CONTRACT, Set.of(
                OrderStatus.CANCELLED,
                OrderStatus.CONTRACT_SENT
        ));

        validTransitions.put(OrderStatus.CONTRACT_SENT, Set.of(
                OrderStatus.CANCELLED,
                OrderStatus.CONTRACT_SIGNED,
                OrderStatus.CONTRACT_DISCUSS
        ));

        validTransitions.put(OrderStatus.CONTRACT_DISCUSS, Set.of(
                OrderStatus.CANCELLED,
                OrderStatus.CONTRACT_SENT
        ));

        validTransitions.put(OrderStatus.CONTRACT_SIGNED, Set.of(
                OrderStatus.CANCELLED,
                OrderStatus.CONTRACT_CONFIRMED,
                OrderStatus.CONTRACT_RESIGNED
        ));

        validTransitions.put(OrderStatus.CONTRACT_RESIGNED, Set.of(
                OrderStatus.CANCELLED,
                OrderStatus.CONTRACT_SIGNED
        ));

        validTransitions.put(OrderStatus.CONTRACT_CONFIRMED, Set.of(
                OrderStatus.CANCELLED,
                OrderStatus.DEPOSITED
        ));

        validTransitions.put(OrderStatus.DEPOSITED, Set.of(
                OrderStatus.CANCELLED,
                OrderStatus.IN_PROGRESS
        ));

        validTransitions.put(OrderStatus.IN_PROGRESS, Set.of(
                OrderStatus.CANCELLED,
                OrderStatus.PRODUCING
        ));

        validTransitions.put(OrderStatus.PRODUCING, Set.of(
                OrderStatus.CANCELLED,
                OrderStatus.PRODUCTION_COMPLETED
        ));

        validTransitions.put(OrderStatus.PRODUCTION_COMPLETED, Set.of(
                OrderStatus.CANCELLED,
                OrderStatus.DELIVERING
        ));

        validTransitions.put(OrderStatus.DELIVERING, Set.of(
                OrderStatus.CANCELLED,
                OrderStatus.INSTALLED
        ));

        validTransitions.put(OrderStatus.INSTALLED, Set.of(
                OrderStatus.ORDER_COMPLETED
        ));

        validTransitions.put(OrderStatus.CANCELLED, Set.of());
        validTransitions.put(OrderStatus.ORDER_COMPLETED, Set.of());
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
