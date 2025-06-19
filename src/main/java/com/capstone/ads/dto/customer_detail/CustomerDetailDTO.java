package com.capstone.ads.dto.customer_detail;

import com.capstone.ads.dto.user.UserDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerDetailDTO {
    String id;
    String logoUrl;
    String companyName;
    String address;
    String contactInfo;
    UserDTO users;
}
