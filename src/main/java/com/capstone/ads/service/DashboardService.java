package com.capstone.ads.service;

import com.capstone.ads.dto.dashboard.AdminDashboardResponse;
import com.capstone.ads.dto.dashboard.CustomerDashboardResponse;
import com.capstone.ads.dto.dashboard.SaleDashboardResponse;
import com.capstone.ads.dto.dashboard.StaffDashboardResponse;

public interface DashboardService {
    CustomerDashboardResponse getCustomerDashboard();
    AdminDashboardResponse getAdminDashboard();
    SaleDashboardResponse getSaleDashboard();
    StaffDashboardResponse getStaffDashboard();
}
