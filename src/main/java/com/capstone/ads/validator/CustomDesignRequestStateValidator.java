package com.capstone.ads.validator;

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
                CustomDesignRequestStatus.CANCELLED
        ));

        validTransitions.put(CustomDesignRequestStatus.PRICING_NOTIFIED, Set.of(
                CustomDesignRequestStatus.APPROVED_PRICING,
                CustomDesignRequestStatus.REJECTED_PRICING,
                CustomDesignRequestStatus.CANCELLED
        ));

        validTransitions.put(CustomDesignRequestStatus.APPROVED_PRICING, Set.of(
                CustomDesignRequestStatus.DEPOSITED,
                CustomDesignRequestStatus.CANCELLED
        ));

        validTransitions.put(CustomDesignRequestStatus.REJECTED_PRICING, Set.of(
                CustomDesignRequestStatus.PRICING_NOTIFIED,
                CustomDesignRequestStatus.CANCELLED
        ));

        validTransitions.put(CustomDesignRequestStatus.DEPOSITED, Set.of(
                CustomDesignRequestStatus.ASSIGNED_DESIGNER,
                CustomDesignRequestStatus.CANCELLED
        ));

        validTransitions.put(CustomDesignRequestStatus.ASSIGNED_DESIGNER, Set.of(
                CustomDesignRequestStatus.DESIGNER_REJECTED,
                CustomDesignRequestStatus.PROCESSING
        ));
        validTransitions.put(CustomDesignRequestStatus.PROCESSING, Set.of(
                CustomDesignRequestStatus.DEMO_SUBMITTED,
                CustomDesignRequestStatus.REVISION_REQUESTED,
                CustomDesignRequestStatus.CANCELLED
        ));

        validTransitions.put(CustomDesignRequestStatus.REVISION_REQUESTED, Set.of(
                CustomDesignRequestStatus.DEMO_SUBMITTED,
                CustomDesignRequestStatus.CANCELLED
        ));

        validTransitions.put(CustomDesignRequestStatus.DEMO_SUBMITTED, Set.of(
                CustomDesignRequestStatus.WAITING_FULL_PAYMENT,
                CustomDesignRequestStatus.REVISION_REQUESTED,
                CustomDesignRequestStatus.CANCELLED
        ));

        validTransitions.put(CustomDesignRequestStatus.WAITING_FULL_PAYMENT, Set.of(
                CustomDesignRequestStatus.FULLY_PAID,
                CustomDesignRequestStatus.CANCELLED
        ));

        validTransitions.put(CustomDesignRequestStatus.FULLY_PAID, Set.of(
                CustomDesignRequestStatus.COMPLETED,
                CustomDesignRequestStatus.CANCELLED
        ));

        validTransitions.put(CustomDesignRequestStatus.CANCELLED, Set.of());
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
