package com.capstone.ads.dto.order_detail;

import com.capstone.ads.model.enums.OrderType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailCreateRequest {
    String customDesignRequestId;
    String editedDesignId;
    String customerChoiceId;
    Long quantity;
}
