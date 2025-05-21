package com.capstone.ads.dto.order;

import com.capstone.ads.dto.user.UserDTO;
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
     private String orderId; // Renamed from id
     private Double totalAmount;
     private Double depositAmount;
     private Double remainingAmount;
     private String note;
     private Boolean isCustomDesign;
     private Timestamp deliveryDate;
     private List<String> histories;
     private OrderStatus status;
     private LocalDateTime orderDate;
     private UserDTO user; // Renamed from userId
     private String aiDesignId;
}
