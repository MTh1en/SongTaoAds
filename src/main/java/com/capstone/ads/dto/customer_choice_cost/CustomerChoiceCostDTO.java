package com.capstone.ads.dto.customer_choice_cost;

import com.capstone.ads.dto.CoreDTO;
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
    CoreDTO customerChoices;
    CoreDTO costTypes;
}
