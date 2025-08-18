package com.capstone.ads.dto.dashboard;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderStaffDashboardResponse {
    int total;
    int producing;
    int productionCompleted;
    int delivering;
    int installed;
    int orderCompleted;
    int cancelled;
}
