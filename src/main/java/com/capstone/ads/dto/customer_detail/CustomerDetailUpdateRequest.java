package com.capstone.ads.dto.customer_detail;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerDetailUpdateRequest {
    String companyName;
    String address;
    String contactInfo;
}
