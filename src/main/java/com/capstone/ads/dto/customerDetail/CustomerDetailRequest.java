package com.capstone.ads.dto.customerDetail;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerDetailRequest {
     String logoUrl;
     String companyName;
     String tagLine;
     String contactInfo;
     String userId;
}
