package com.capstone.ads.dto.dashboard;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDashboardResponse {
    long revenue;
    long payOSRevenue;
    long castRevenue;
    long designRevenue;
    long constructionRevenue;
}
