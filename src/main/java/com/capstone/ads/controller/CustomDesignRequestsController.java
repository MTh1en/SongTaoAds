package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.customdesignrequest.CustomDesignRequestCreateRequest;
import com.capstone.ads.dto.customdesignrequest.CustomDesignRequestDTO;
import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import com.capstone.ads.service.CustomDesignRequestService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/custom-design-requests/{customDesignRequestId}/users/{designerId}")
    public ApiResponse<CustomDesignRequestDTO> assignDesignerToCustomerRequest(@PathVariable String customDesignRequestId,
                                                                               @PathVariable String designerId) {
        var response = service.assignDesignerToCustomerRequest(customDesignRequestId, designerId);
        return ApiResponseBuilder.buildSuccessResponse("Assign custom design request successful", response);
    }

    @PatchMapping("/custom-design-requests/{customDesignRequestId}/status")
    public ApiResponse<CustomDesignRequestDTO> changeCustomDesignRequestStatus(@PathVariable String customDesignRequestId,
                                                                               @RequestParam("status") CustomDesignRequestStatus status) {
        var response = service.changeStatusCustomDesignRequest(customDesignRequestId, status);
        return ApiResponseBuilder.buildSuccessResponse("Change custom design request status successful", response);
    }

    @GetMapping("/customer-details/{customerDetailId}/custom-design-requests")
    public ApiPagingResponse<CustomDesignRequestDTO> findCustomerDesignRequestByCustomerDetailId(@PathVariable String customerDetailId,
                                                                                                 @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                                                                 @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = service.findCustomerDesignRequestByCustomerDetailId(customerDetailId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Find custom design request by custom detail request successful", response, page);
    }

    @GetMapping("/users/{designerId}/custom-design-requests")
    public ApiPagingResponse<CustomDesignRequestDTO> findCustomerDetailRequestByAssignDesignerId(@PathVariable String designerId,
                                                                                                 @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                                                                 @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = service.findCustomerDetailRequestByAssignDesignerId(designerId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Find custom design request by designer request successful", response, page);
    }

    @GetMapping("/custom-design-requests")
    public ApiPagingResponse<CustomDesignRequestDTO> findCustomerDetailRequestByStatus(@RequestParam("status") CustomDesignRequestStatus status,
                                                                                       @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                                                       @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = service.findCustomerDetailRequestByStatus(status, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Find custom design request by status successful", response, page);
    }
}
