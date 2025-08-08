package com.capstone.ads.dto.chatBot;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TraditionalBillboardResponse {
    String frameCost;
    String frameCostFormula;
    String backgroundCost;
    String backgroundCostFormula;
    String borderCost;
    String borderCostFormula;
    String numberOfFacesCost;
    String numberOfFacesCostFormula;
    String installationMethodCost;
    String installationMethodCostFormula;
    String billboardFaceCost;
    String billboardFaceCostFormula;
    String totalCostFormula;
    String total;
}
