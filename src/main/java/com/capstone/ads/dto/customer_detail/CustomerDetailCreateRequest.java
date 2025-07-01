package com.capstone.ads.dto.customer_detail;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerDetailCreateRequest {
    String companyName;
    String address;
    String contactInfo;
    MultipartFile customerDetailLogo;
}
