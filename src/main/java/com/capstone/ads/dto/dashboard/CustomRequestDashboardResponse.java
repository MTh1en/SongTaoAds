package com.capstone.ads.dto.dashboard;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomRequestDashboardResponse {
    int total;
    int pending;
    int pricingNotified;
    int rejectedPricing;
    int approvedPricing;
    int deposited;
    int assignedDesigner;
    int processing;
    int designerRejected;
    int demoSubmitted;
    int revisionRequested;
    int waitingFullPayment;
    int fullyPaid;
    int completed;
    int cancelled;
}
