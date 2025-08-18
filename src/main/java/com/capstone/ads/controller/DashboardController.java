package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.dashboard.*;
import com.capstone.ads.service.DashboardService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "DASHBOARD")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/admin")
    @Operation(summary = "Dashboard cho Admin")
    public ApiResponse<AdminDashboardResponse> adminDashboard() {
        var response = dashboardService.getAdminDashboard();
        return ApiResponseBuilder.buildSuccessResponse("Response retrieved successfully.", response);
    }

    @GetMapping("/sale")
    @Operation(summary = "Dashboard cho Sale")
    public ApiResponse<SaleDashboardResponse> saleDashboard() {
        var response = dashboardService.getSaleDashboard();
        return ApiResponseBuilder.buildSuccessResponse("Response retrieved successfully.", response);
    }

    @GetMapping("/staff")
    @Operation(summary = "Dashboard cho Staff")
    public ApiResponse<StaffDashboardResponse> staffDashboard() {
        var response = dashboardService.getStaffDashboard();
        return ApiResponseBuilder.buildSuccessResponse("Response retrieved successfully.", response);
    }

    @GetMapping("/designer")
    @Operation(summary = "Dashboard cho Designer")
    public ApiResponse<DesignerDashboardResponse> designerDashboard() {
        var response = dashboardService.getDesignerDashboard();
        return ApiResponseBuilder.buildSuccessResponse("Response retrieved successfully.", response);
    }

    @PostMapping("/custom-design-requests")
    @Operation(summary = "Dashboard cho Custom Design Request")
    public ApiResponse<CustomRequestDashboardResponse> customDesignRequestDashboard(@RequestBody TimeRangeRequest request) {
        var response = dashboardService.customDesignRequestDashboard(request);
        return ApiResponseBuilder.buildSuccessResponse("Response retrieved successfully.", response);
    }

    @PostMapping("/orders/sale")
    @Operation(summary = "Dashboard cho Order của Sale")
    public ApiResponse<OrderSaleDashboardResponse> orderSaleDashboard(@RequestBody TimeRangeRequest request) {
        var response = dashboardService.orderSaleDashboard(request);
        return ApiResponseBuilder.buildSuccessResponse("Response retrieved successfully.", response);
    }

    @PostMapping("/orders/staff")
    @Operation(summary = "Dashboard cho Order của staff")
    public ApiResponse<OrderStaffDashboardResponse> orderStaffDashboard(@RequestBody TimeRangeRequest request) {
        var response = dashboardService.orderStaffDashboard(request);
        return ApiResponseBuilder.buildSuccessResponse("Response retrieved successfully.", response);
    }

    @PostMapping("/payments")
    @Operation(summary = "Dashboard lợi nhuận")
    public ApiResponse<PaymentDashboardResponse> paymentDashboard(@RequestBody TimeRangeRequest request) {
        var response = dashboardService.paymentDashboard(request);
        return ApiResponseBuilder.buildSuccessResponse("Response retrieved successfully.", response);
    }
}
