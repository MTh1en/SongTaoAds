package com.capstone.ads.dto.price_proposal;

import com.capstone.ads.dto.CoreDTO;
import com.capstone.ads.model.enums.PriceProposalStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PriceProposalDTO {
    String id;
    Long totalPrice;
    Long depositAmount;
    String rejectionReason;
    Long totalPriceOffer;
    Long depositAmountOffer;
    PriceProposalStatus status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    CoreDTO customDesignRequests;
}
