package com.capstone.ads.dto.custom_design_request;

import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import com.capstone.ads.model.json.CustomerChoiceHistories;
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
    String requirements;
    Long totalPrice;
    Long depositAmount;
    Long remainingAmount;
    String finalDesignImage;
    Boolean isNeedSupport;
    Boolean hasOrder;
    CustomerChoiceHistories customerChoiceHistories;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    CustomDesignRequestStatus status;
    String customerDetail;
    String assignDesigner;
}
