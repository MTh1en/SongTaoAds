package com.capstone.ads.dto.order;

import com.capstone.ads.dto.CoreDTO;
import com.capstone.ads.dto.user.UserDTO;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.model.enums.OrderType;
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
    Long totalConstructionAmount;
    Long depositConstructionAmount;
    Long remainingConstructionAmount;
    Long totalDesignAmount;
    Long depositDesignAmount;
    Long remainingDesignAmount;
    String note;
    OrderType orderType;
    OrderStatus status;
    CoreDTO contractors;
    UserDTO users;
}
