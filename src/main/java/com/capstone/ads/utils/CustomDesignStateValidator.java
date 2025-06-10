package com.capstone.ads.utils;

import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.model.enums.CustomDesignStatus;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Component
public class CustomDesignStateValidator {
    private final Map<CustomDesignStatus, Set<CustomDesignStatus>> validTransitions;

    public CustomDesignStateValidator() {
        this.validTransitions = new EnumMap<>(CustomDesignStatus.class);
        initializeValidTransitions();
    }

    private void initializeValidTransitions() {
        validTransitions.put(CustomDesignStatus.PENDING, Set.of(
                CustomDesignStatus.APPROVED,
                CustomDesignStatus.REJECTED
        ));

        validTransitions.put(CustomDesignStatus.REJECTED, Set.of());
        validTransitions.put(CustomDesignStatus.APPROVED, Set.of());
    }

    public boolean isValidTransition(CustomDesignStatus currentStatus, CustomDesignStatus newStatus) {
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

    public void validateTransition(CustomDesignStatus currentStatus,
                                   CustomDesignStatus newStatus) {
        if (!isValidTransition(currentStatus, newStatus)) {
            throw new AppException(ErrorCode.INVALID_CUSTOM_DESIGN_STATUS_TRANSITION);
        }
    }
}
