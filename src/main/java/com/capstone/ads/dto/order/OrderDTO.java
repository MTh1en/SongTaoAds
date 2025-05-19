package com.capstone.ads.dto.order;

import com.capstone.ads.model.enums.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDTO {
     Double totalAmount;
     Double depositAmount;
     Double remainingAmount;
     String note;
     Boolean isCustomDesign;
     Timestamp deliveryDate;
     List<String> histories;
     OrderStatus status;
     String userId;
     String aiDesignId;
}
