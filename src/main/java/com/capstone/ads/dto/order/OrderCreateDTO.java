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
         String id;
         Double totalAmount;
         String note;
         Boolean isCustomDesign;
         List<String> histories;
         String userId; // Changed from Long to String
         String aiDesignId; // Changed from Long to String
}

