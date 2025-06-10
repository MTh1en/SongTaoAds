package com.capstone.ads.dto.order;

import com.capstone.ads.dto.aidesign.AIDesignDTO;
import com.capstone.ads.dto.customdesign.CustomDesignDTO;
import com.capstone.ads.dto.user.UserDTO;
import com.capstone.ads.model.AIDesigns;
import com.capstone.ads.model.CustomDesigns;
import com.capstone.ads.model.Users;
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
    String id;
    Double totalAmount;
    Double depositAmount;
    Double remainingAmount;
    String address;
    String note;
    LocalDateTime deliveryDate;
    CustomerChoiceHistories customerChoiceHistories;
    OrderStatus status;
    LocalDateTime orderDate;
    UserDTO users;
    OrderAIDesignDTO aiDesigns;
    OrderCustomDesignDTO customDesigns;
}
