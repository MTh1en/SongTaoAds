package com.capstone.ads.dto.customdesign;

import com.capstone.ads.model.enums.CustomDesignStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerDecisionCustomDesignRequest {
    String customerNote;
    CustomDesignStatus status;
}
