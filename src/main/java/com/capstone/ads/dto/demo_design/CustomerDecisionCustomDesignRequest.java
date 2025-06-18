package com.capstone.ads.dto.demo_design;

import com.capstone.ads.model.enums.DemoDesignStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerDecisionCustomDesignRequest {
    String customerNote;
    DemoDesignStatus status;
}
