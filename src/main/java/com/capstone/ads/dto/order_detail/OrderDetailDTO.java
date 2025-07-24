package com.capstone.ads.dto.order_detail;

import com.capstone.ads.dto.CoreDTO;
import com.capstone.ads.model.json.CustomerChoiceHistories;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailDTO {
    String id;
    Long detailConstructionAmount;
    Long quantity;
    Long detailDesignAmount;
    Long detailDepositDesignAmount;
    Long detailRemainingDesignAmount;
    CustomerChoiceHistories customerChoiceHistories;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    CoreDTO editedDesigns;
    CoreDTO customDesignRequests;
    CoreDTO orders;
}
