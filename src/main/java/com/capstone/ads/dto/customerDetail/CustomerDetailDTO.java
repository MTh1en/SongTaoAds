package com.capstone.ads.dto.customerDetail;

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
    String tagLine;
    String contactInfo;
    UserDTO users;
}
