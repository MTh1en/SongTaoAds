package com.capstone.ads.dto.chatBot;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModernBillboardResponse {
    String frameCost;
    String backgroundCost;
    String borderCost;
    String textAndLogoCost;
    String textSpecificationCost;
    String installationMethodCost;
    String totalCostFormula;
    String totalCost;
}
