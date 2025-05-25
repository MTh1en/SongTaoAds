package com.capstone.ads.dto.order;

import com.capstone.ads.dto.user.UserDTO;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.model.json.OrderHistory;
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
    String id; // Renamed from id
    Integer totalAmount;
    Integer depositAmount;
    Integer remainingAmount;
    String address;
    String note;
    Boolean isCustomDesign;
    Timestamp deliveryDate;
    OrderHistory histories;
    OrderStatus status;
    LocalDateTime orderDate;
    UserDTO users;
    String aiDesignId;
}
