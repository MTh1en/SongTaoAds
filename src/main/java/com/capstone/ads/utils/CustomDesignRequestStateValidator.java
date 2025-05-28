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
                CustomDesignRequestStatus.APPROVED,
                CustomDesignRequestStatus.REJECTED,
                CustomDesignRequestStatus.CANCEL
        ));

        validTransitions.put(CustomDesignRequestStatus.APPROVED, Set.of(
                CustomDesignRequestStatus.PROCESSING
        ));

        validTransitions.put(CustomDesignRequestStatus.PROCESSING, Set.of(
                CustomDesignRequestStatus.COMPLETED
        ));

        // REJECTED, CANCEL, COMPLETED are terminal states with no valid transitions
        validTransitions.put(CustomDesignRequestStatus.REJECTED, Set.of());
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
