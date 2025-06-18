package com.capstone.ads.utils;

import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.model.enums.DemoDesignStatus;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Component
public class DemoDesignStateValidator {
    private final Map<DemoDesignStatus, Set<DemoDesignStatus>> validTransitions;

    public DemoDesignStateValidator() {
        this.validTransitions = new EnumMap<>(DemoDesignStatus.class);
        initializeValidTransitions();
    }

    private void initializeValidTransitions() {
        validTransitions.put(DemoDesignStatus.PENDING, Set.of(
                DemoDesignStatus.APPROVED,
                DemoDesignStatus.REJECTED
        ));

        validTransitions.put(DemoDesignStatus.REJECTED, Set.of());
        validTransitions.put(DemoDesignStatus.APPROVED, Set.of());
    }

    public boolean isValidTransition(DemoDesignStatus currentStatus, DemoDesignStatus newStatus) {
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

    public void validateTransition(DemoDesignStatus currentStatus,
                                   DemoDesignStatus newStatus) {
        if (!isValidTransition(currentStatus, newStatus)) {
            throw new AppException(ErrorCode.INVALID_CUSTOM_DESIGN_STATUS_TRANSITION);
        }
    }
}
