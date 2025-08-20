package com.capstone.ads.service;

import com.capstone.ads.dto.dashboard.*;

public interface DashboardService {
    AdminDashboardResponse getAdminDashboard();

    SaleDashboardResponse getSaleDashboard();

    StaffDashboardResponse getStaffDashboard();

    DesignerDashboardResponse getDesignerDashboard();

    CustomRequestDashboardResponse customDesignRequestDashboard(TimeRangeRequest request);

    OrderSaleDashboardResponse orderSaleDashboard(TimeRangeRequest request);

    OrderStaffDashboardResponse orderStaffDashboard(TimeRangeRequest request);

    PaymentDashboardResponse paymentDashboard(TimeRangeRequest request);
}
