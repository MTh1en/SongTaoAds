package com.capstone.ads.utils;

import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Component
public class CustomDesignRequestStateValidator {
    private final Map<CustomDesignRequestStatus, Set<CustomDesignRequestStatus>> validTransitions;

    public CustomDesignRequestStateValidator() {
        this.validTransitions = new EnumMap<>(CustomDesignRequestStatus.class);
        initializeValidTransitions();
    }

    private void initializeValidTransitions() {
        validTransitions.put(CustomDesignRequestStatus.PENDING, Set.of(
                CustomDesignRequestStatus.PRICING_NOTIFIED,
                CustomDesignRequestStatus.CANCEL
        ));

        validTransitions.put(CustomDesignRequestStatus.PRICING_NOTIFIED, Set.of(
                CustomDesignRequestStatus.APPROVED_PRICING,
                CustomDesignRequestStatus.REJECTED_PRICING,
                CustomDesignRequestStatus.CANCEL
        ));

        validTransitions.put(CustomDesignRequestStatus.APPROVED_PRICING, Set.of(
                CustomDesignRequestStatus.DEPOSITED,
                CustomDesignRequestStatus.CANCEL
        ));

        validTransitions.put(CustomDesignRequestStatus.REJECTED_PRICING, Set.of(
                CustomDesignRequestStatus.PRICING_NOTIFIED,
                CustomDesignRequestStatus.CANCEL
        ));

        validTransitions.put(CustomDesignRequestStatus.DEPOSITED, Set.of(
                CustomDesignRequestStatus.PROCESSING,
                CustomDesignRequestStatus.DESIGNER_REJECTED,
                CustomDesignRequestStatus.CANCEL
        ));

        validTransitions.put(CustomDesignRequestStatus.PROCESSING, Set.of(
                CustomDesignRequestStatus.DEMO_SUBMITTED,
                CustomDesignRequestStatus.REVISION_REQUESTED,
                CustomDesignRequestStatus.CANCEL
        ));

        validTransitions.put(CustomDesignRequestStatus.REVISION_REQUESTED, Set.of(
                CustomDesignRequestStatus.DEMO_SUBMITTED,
                CustomDesignRequestStatus.CANCEL
        ));

        validTransitions.put(CustomDesignRequestStatus.DEMO_SUBMITTED, Set.of(
                CustomDesignRequestStatus.WAITING_FULL_PAYMENT,
                CustomDesignRequestStatus.CANCEL
        ));

        validTransitions.put(CustomDesignRequestStatus.WAITING_FULL_PAYMENT, Set.of(
                CustomDesignRequestStatus.FULLY_PAID,
                CustomDesignRequestStatus.CANCEL
        ));

        validTransitions.put(CustomDesignRequestStatus.FULLY_PAID, Set.of(
                CustomDesignRequestStatus.COMPLETED,
                CustomDesignRequestStatus.CANCEL
        ));

        validTransitions.put(CustomDesignRequestStatus.CANCEL, Set.of());
        validTransitions.put(CustomDesignRequestStatus.COMPLETED, Set.of());
    }

    public boolean isValidTransition(CustomDesignRequestStatus currentStatus,
                                     CustomDesignRequestStatus newStatus) {
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

    public void validateTransition(CustomDesignRequestStatus currentStatus,
                                   CustomDesignRequestStatus newStatus) {
        if (!isValidTransition(currentStatus, newStatus)) {
            throw new AppException(ErrorCode.INVALID_CUSTOM_DESIGN_REQUEST_STATUS_TRANSITION);
        }
    }
}
