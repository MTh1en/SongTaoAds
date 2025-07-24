package com.capstone.ads.dto.dashboard;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffDashboardResponse {
    int productingOrders;
    int productionCompletedOrders;
    int inprogressTickets;
    int completedOrders;
}
