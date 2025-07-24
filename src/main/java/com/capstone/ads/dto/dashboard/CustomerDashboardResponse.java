package com.capstone.ads.dto.dashboard;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerDashboardResponse {
    int totalOrders;
    int contractConfirmedOrders;
    int depositedOrders;
    int productionCompleteOrders;
    int completeOrders;
    int closeTickets;
    long totalPayments;
}
