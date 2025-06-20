package com.capstone.ads.dto.order;

import com.capstone.ads.dto.user.UserDTO;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.model.json.CustomerChoiceHistories;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDTO {
    String id;
    String address;
    Double totalAmount;
    Double depositAmount;
    Double remainingAmount;
    String note;

    String draftImageUrl;
    String productImageUrl;
    String deliveryImageUrl;
    String installationImageUrl;


    LocalDateTime estimatedDeliveryDate;
    LocalDateTime depositPaidDate;
    LocalDateTime productionStartDate;
    LocalDateTime productionEndDate;
    LocalDateTime deliveryStartDate;
    LocalDateTime actualDeliveryDate;
    LocalDateTime completionDate;

    LocalDateTime orderDate;
    LocalDateTime updateDate;

    CustomerChoiceHistories customerChoiceHistories;
    OrderStatus status;
    UserDTO users;
    OrderAIDesignDTO aiDesigns;
    OrderCustomDesignDTO customDesignRequests;
}
