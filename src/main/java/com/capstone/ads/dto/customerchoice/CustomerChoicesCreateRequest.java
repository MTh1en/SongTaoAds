package com.capstone.ads.dto.customerchoice;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerChoicesCreateRequest {
    Double totalAmount;
    Boolean isFinal;
}
