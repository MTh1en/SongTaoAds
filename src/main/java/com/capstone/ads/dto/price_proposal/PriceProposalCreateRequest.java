package com.capstone.ads.dto.price_proposal;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Range;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PriceProposalCreateRequest {
    @Range(min = 1000, message = "Total price must be greater than 1.000VNĐ")
    Integer totalPrice;
    @Range(min = 1000, message = "Deposit amount must be greater than 1.000VNĐ")
    Integer depositAmount;
}
