package com.capstone.ads.validator;

import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.model.enums.ContractStatus;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Component
public class ContractStateValidator {
    private final Map<ContractStatus, Set<ContractStatus>> validTransitions;

    public ContractStateValidator() {
        this.validTransitions = new EnumMap<>(ContractStatus.class);
        initializeValidTransitions();
    }

    private void initializeValidTransitions() {
        validTransitions.put(ContractStatus.SENT, Set.of(
                ContractStatus.NEED_DISCUSS,
                ContractStatus.SIGNED
        ));

        validTransitions.put(ContractStatus.NEED_DISCUSS, Set.of(
                ContractStatus.SENT
        ));
        validTransitions.put(ContractStatus.NEED_RESIGNED, Set.of(
                ContractStatus.SIGNED
        ));
        validTransitions.put(ContractStatus.SIGNED, Set.of(
                ContractStatus.NEED_RESIGNED
        ));
    }

    public boolean isValidTransition(ContractStatus currentStatus, ContractStatus newStatus) {
        if (currentStatus == null || newStatus == null) {
            return false;
        }

        if (currentStatus == newStatus) {
            return false;
        }
        return validTransitions.getOrDefault(currentStatus, Set.of())
                .contains(newStatus);
    }

    public void validateTransition(ContractStatus currentStatus,
                                   ContractStatus newStatus) {
        if (!isValidTransition(currentStatus, newStatus)) {
            throw new AppException(ErrorCode.INVALID_CUSTOM_DESIGN_STATUS_TRANSITION);
        }
    }
}
