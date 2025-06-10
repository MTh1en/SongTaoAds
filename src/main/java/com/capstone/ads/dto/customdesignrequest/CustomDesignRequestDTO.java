package com.capstone.ads.dto.customdesignrequest;

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
    CustomerChoiceHistories customerChoiceHistories;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    CustomDesignRequestStatus status;
    String customerDetail;
    String assignDesigner;
}
