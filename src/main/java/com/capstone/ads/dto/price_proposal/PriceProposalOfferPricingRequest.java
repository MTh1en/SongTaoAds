package com.capstone.ads.dto.price_proposal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Range;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PriceProposalOfferPricingRequest {
    @NotBlank(message = "Rejection Reason is Required")
    String rejectionReason;
    @Range(min = 1000, message = "Total price offer must be greater than 1.000VNĐ")
    Integer totalPriceOffer;
    @Range(min = 1000, message = "Deposit amount offer must be greater than 1.000VNĐ")
    Integer depositAmountOffer;
}
