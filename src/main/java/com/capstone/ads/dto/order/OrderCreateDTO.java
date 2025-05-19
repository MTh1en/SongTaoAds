package com.capstone.ads.dto.order;


import com.capstone.ads.model.enums.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderCreateDTO {
        private Double totalAmount;
        private Double depositAmount;
        private Double remainingAmount;
        private String note;
        private Boolean isCustomDesign;
        private List<String> histories;
        private String userId; // Changed from Long to String
        private String aiDesignId; // Changed from Long to String
}

