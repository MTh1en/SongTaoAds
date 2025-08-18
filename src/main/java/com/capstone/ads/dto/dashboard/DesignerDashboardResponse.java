package com.capstone.ads.dto.dashboard;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DesignerDashboardResponse {
    int totalCustomDesignRequestAssigned;
    int totalDemoSubmitted;
    int totalDemoApproved;
    int totalDemoRejected;
    int totalFinalDesignSubmitted;
}
