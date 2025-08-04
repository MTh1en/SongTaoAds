package com.capstone.ads.dto.custom_design_request;

import com.capstone.ads.dto.customer_detail.CustomerDetailDTO;
import com.capstone.ads.dto.user.UserDTO;
import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomDesignRequestDTO {
    String id;
    String code;
    String requirements;
    Long totalPrice;
    Long depositAmount;
    Long remainingAmount;
    String finalDesignImage;
    Boolean isNeedSupport;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    CustomDesignRequestStatus status;
    CustomerDetailDTO customerDetail;
    UserDTO assignDesigner;
}
