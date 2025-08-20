package com.capstone.ads.dto.dashboard;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SaleDashboardResponse {
    int totalOrders;
    int totalOrderCompleted;
    int totalOrderInProgress;
    int totalOrderCancelled;
    int totalAiDesignOrder;
    int totalCustomDesignOrder;
    int totalCustomDesignRequest;
    int totalCustomDesignRequestCompleted;
    int totalCustomDesignRequestInProgress;
    int totalCustomDesignRequestCancelled;
    long totalRevenue;
    long totalPayOSPayment;
    long totalCastPayment;
    long totalDesignPaid;
    long totalOrderPaid;
    int totalContractSigned;
    int totalFeedback;
    int totalFeedbackResponse;
    int totalTicket;
    int totalTicketInProgress;
    int totalTicketClosed;
    int totalTicketDelivered;
}
