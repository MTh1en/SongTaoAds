package com.capstone.ads.dto.dashboard;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SaleDashboardResponse {
    long totalOrders;
    long totalPayments;
    long totalDeposited;
    int totalPendingDesignOrders;
    int totalPendingContractOrders;
    int totalDepositedOrders;
    int pendingTickets;
    long totalFeedback;
    long totalContracts;
}
