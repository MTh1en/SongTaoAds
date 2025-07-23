package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.dashboard.AdminDashboardResponse;
import com.capstone.ads.dto.dashboard.CustomerDashboardResponse;
import com.capstone.ads.dto.dashboard.SaleDashboardResponse;
import com.capstone.ads.dto.dashboard.StaffDashboardResponse;
import com.capstone.ads.service.DashboardService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "DASHBOARD")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/customer")
    @Operation(summary = "Dashboard cho Customer")
    public ApiResponse<CustomerDashboardResponse> customerDashboard() {
        CustomerDashboardResponse response = dashboardService.getCustomerDashboard();
        return ApiResponseBuilder.buildSuccessResponse("Response retrieved successfully.", response);
    }

    @GetMapping("/admin")
    @Operation(summary = "Dashboard cho Admin")
    public ApiResponse<AdminDashboardResponse> adminDashboard() {
        AdminDashboardResponse response = dashboardService.getAdminDashboard();
        return ApiResponseBuilder.buildSuccessResponse("Response retrieved successfully.", response);
    }

    @GetMapping("/sale")
    @Operation(summary = "Dashboard cho Sale")
    public ApiResponse<SaleDashboardResponse> saleDashboard() {
        SaleDashboardResponse response = dashboardService.getSaleDashboard();
        return ApiResponseBuilder.buildSuccessResponse("Response retrieved successfully.", response);
    }

    @GetMapping("/customer")
    @Operation(summary = "Dashboard cho Staff")
    public ApiResponse<StaffDashboardResponse> staffDashboard() {
        StaffDashboardResponse response = dashboardService.getStaffDashboard();
        return ApiResponseBuilder.buildSuccessResponse("Response retrieved successfully.", response);
    }
}
