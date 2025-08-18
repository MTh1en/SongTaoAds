package com.capstone.ads.dto.dashboard;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffDashboardResponse {
    int totalOrder;
    int totalProducingOrder;
    int totalProductionCompletedOrder;
    int totalDeliveringOrder;
    int totalInstalledOrder;
    int totalProductType;
    int totalProductTypeActive;
    int totalProductTypeUsingAI;
    int totalAttribute;
    int totalAttributeActive;
    int totalAttributeValue;
    int totalAttributeValueActive;
    int totalCostType;
    int totalCostTypeActive;
    int totalDesignTemplate;
    int totalDesignTemplateActive;
    int totalBackground;
    int totalBackgroundActive;
    int totalModelChatBot;
    int totalTopic;
    int totalQuestion;
    int totalContractor;
    int totalContractorActive;
    int totalContactorInternal;
    int totalContractorExternal;
    long totalRevenue;
    long totalPayOSPayment;
    long totalCastPayment;
}
