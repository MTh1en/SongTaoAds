package com.capstone.ads.dto.customer_choice_cost;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerChoiceCostDTO {
    String id;
    Long value;
    String customerChoiceId;
    String costTypeId;
}
