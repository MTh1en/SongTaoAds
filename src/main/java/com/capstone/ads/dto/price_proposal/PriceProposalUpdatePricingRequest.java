package com.capstone.ads.dto.price_proposal;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PriceProposalUpdatePricingRequest {
    @Size(min = 1000, message = "Total price must be greater than 1.000VNĐ")
    Integer totalPrice;
    @Size(min = 1000, message = "Deposit amount must be greater than 1.000VNĐ")
    Integer depositAmount;
}
