package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.customdesignrequest.CustomDesignRequestCreateRequest;
import com.capstone.ads.dto.customdesignrequest.CustomDesignRequestDTO;
import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import com.capstone.ads.service.CustomDesignRequestService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class CustomDesignRequestsController {
    private final CustomDesignRequestService service;

    @PostMapping("/customer-details/{customerDetailId}/customer-choices/{customerChoiceId}")
    public ApiResponse<CustomDesignRequestDTO> createCustomDesignRequest(@PathVariable String customerDetailId,
                                                                         @PathVariable String customerChoiceId,
                                                                         @RequestBody CustomDesignRequestCreateRequest request) {
        var response = service.createCustomDesignRequest(customerDetailId, customerChoiceId, request);
        return ApiResponseBuilder.buildSuccessResponse("Create custom design request successful", response);
    }

    @PutMapping("/custom-design-requests/{customDesignRequestId}/users/{designerId}")
    public ApiResponse<CustomDesignRequestDTO> assignDesignerToCustomerRequest(@PathVariable String customDesignRequestId,
                                                                               @PathVariable String designerId) {
        var response = service.assignDesignerToCustomerRequest(customDesignRequestId, designerId);
        return ApiResponseBuilder.buildSuccessResponse("Assign custom design request successful", response);
    }

    @PutMapping("/custom-design-requests/{customDesignRequestId}")
    public ApiResponse<CustomDesignRequestDTO> changeCustomDesignRequestStatus(@PathVariable String customDesignRequestId,
                                                                               @RequestParam("status") CustomDesignRequestStatus status) {
        var response = service.changeStatusCustomDesignRequest(customDesignRequestId, status);
        return ApiResponseBuilder.buildSuccessResponse("Change custom design request status successful", response);
    }

    @GetMapping("/customer-details/{customerDetailId}/custom-design-requests")
    public ApiResponse<List<CustomDesignRequestDTO>> findCustomerDesignRequestByCustomerDetailId(@PathVariable String customerDetailId) {
        var response = service.findCustomerDesignRequestByCustomerDetailId(customerDetailId);
        return ApiResponseBuilder.buildSuccessResponse("Find custom design request by custom detail request successful", response);
    }

    @GetMapping("/users/{designerId}/custom-design-requests")
    public ApiResponse<List<CustomDesignRequestDTO>> findCustomerDetailRequestByAssignDesignerId(@PathVariable String designerId) {
        var response = service.findCustomerDetailRequestByAssignDesignerId(designerId);
        return ApiResponseBuilder.buildSuccessResponse("Find custom design request by designer request successful", response);
    }

    @GetMapping("/custom-design-requests")
    public ApiResponse<List<CustomDesignRequestDTO>> findCustomerDetailRequestByStatus(@RequestParam("status") CustomDesignRequestStatus status) {
        var response = service.findCustomerDetailRequestByStatus(status);
        return ApiResponseBuilder.buildSuccessResponse("Find custom design request by status successful", response);
    }

}
