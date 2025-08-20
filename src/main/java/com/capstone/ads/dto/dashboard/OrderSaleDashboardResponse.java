package com.capstone.ads.dto.dashboard;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderSaleDashboardResponse {
    int total;
    int pendingDesign;
    int needDepositDesign;
    int depositedDesign;
    int needFullyPaidDesign;
    int waitingFinalDesign;
    int designCompleted;

    int pendingContract;
    int contractSent;
    int contractSigned;
    int contractDiscuss;
    int contractResigned;
    int contractConfirmed;

    int deposited;
    int inProgress;
    int cancelled;
}
