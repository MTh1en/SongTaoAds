package com.capstone.ads.dto.customer_detail;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerDetailCreateRequest {
    @NotBlank(message = "Company Name is required")
    String companyName;
    @NotBlank(message = "Address is required")
    String address;
    @NotBlank(message = "Contact info is required")
    String contactInfo;
    MultipartFile customerDetailLogo;
}
