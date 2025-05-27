package com.capstone.ads.dto.order;

import com.capstone.ads.dto.user.UserDTO;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.model.json.CustomerChoiceHistories;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDTO {
    String id; // Renamed from id
    Double totalAmount;
    Double depositAmount;
    Double remainingAmount;
    String address;
    String note;
    Boolean isCustomDesign;
    Timestamp deliveryDate;
    CustomerChoiceHistories histories;
    OrderStatus status;
    LocalDateTime orderDate;
    UserDTO users;
    String aiDesignId;
}
