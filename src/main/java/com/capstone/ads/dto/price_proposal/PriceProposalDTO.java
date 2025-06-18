package com.capstone.ads.dto.price_proposal;

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
    Integer totalPrice;
    Integer depositAmount;
    String rejectionReason;
    Integer totalPriceOffer;
    Integer depositAmountOffer;
    PriceProposalStatus status;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    String customDesignRequestId;
}
