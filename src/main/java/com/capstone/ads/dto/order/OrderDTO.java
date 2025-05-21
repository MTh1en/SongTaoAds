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
      String orderId; // Renamed from id
      Double totalAmount;
      Double depositAmount;
      Double remainingAmount;
      String address;
      String note;
      Boolean isCustomDesign;
      Timestamp deliveryDate;
      List<String> histories;
      OrderStatus status;
      LocalDateTime orderDate;
      UserDTO user; // Renamed from userId
      String aiDesignId;
}
