package com.capstone.ads.dto.dashboard;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminDashboardResponse {
    int totalUsers;
    int totalBannedUsers;
    int totalCustomer;
    int totalSale;
    int totalStaff;
    int totalDesigner;
    int totalAdmin;
    int totalPaymentTransactionCreated;
    int totalPaymentSuccess;
    int totalPaymentFailure;
    int totalPaymentCancelled;
    long totalPaymentSuccessAmount;
    long totalPaymentFailureAmount;
    long totalPaymentCancelledAmount;
    long totalPayOSSuccessAmount;
    long totalPayOSFailureAmount;
    long totalPayOSCancelledAmount;
    long totalCastAmount;
    int totalImage;
    int totalNotification;
    int totalChatBotUsed;
}
