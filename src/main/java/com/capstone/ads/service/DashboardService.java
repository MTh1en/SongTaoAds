package com.capstone.ads.service;

import com.capstone.ads.dto.dashboard.*;

public interface DashboardService {
    AdminDashboardResponse getAdminDashboard(TimeRangeRequest request);

    SaleDashboardResponse getSaleDashboard(TimeRangeRequest request);

    StaffDashboardResponse getStaffDashboard(TimeRangeRequest request);

    DesignerDashboardResponse getDesignerDashboard(TimeRangeRequest request);

    CustomRequestDashboardResponse customDesignRequestDashboard(TimeRangeRequest request);

    OrderSaleDashboardResponse orderSaleDashboard(TimeRangeRequest request);

    OrderStaffDashboardResponse orderStaffDashboard(TimeRangeRequest request);

    PaymentDashboardResponse paymentDashboard(TimeRangeRequest request);
}
