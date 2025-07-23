package com.capstone.ads.dto.order;

import com.capstone.ads.model.enums.OrderType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderCreateRequest {
    OrderType orderType;
}
