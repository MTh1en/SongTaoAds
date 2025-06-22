package com.capstone.ads.utils;

import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.model.enums.DemoDesignStatus;
import com.capstone.ads.model.enums.PriceProposalStatus;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Component
public class PriceProposalStateValidator {
    private final Map<PriceProposalStatus, Set<PriceProposalStatus>> validTransitions;

    public PriceProposalStateValidator() {
        this.validTransitions = new EnumMap<>(PriceProposalStatus.class);
        initializeValidTransitions();
    }

    private void initializeValidTransitions() {
        validTransitions.put(PriceProposalStatus.PENDING, Set.of(
                PriceProposalStatus.APPROVED,
                PriceProposalStatus.REJECTED
        ));

        validTransitions.put(PriceProposalStatus.REJECTED, Set.of());
        validTransitions.put(PriceProposalStatus.APPROVED, Set.of());
    }

    public boolean isValidTransition(PriceProposalStatus currentStatus, PriceProposalStatus newStatus) {
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

    public void validateTransition(PriceProposalStatus currentStatus,
                                   PriceProposalStatus newStatus) {
        if (!isValidTransition(currentStatus, newStatus)) {
            throw new AppException(ErrorCode.INVALID_CUSTOM_DESIGN_STATUS_TRANSITION);
        }
    }
}
