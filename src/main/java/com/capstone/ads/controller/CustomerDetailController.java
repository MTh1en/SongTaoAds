package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.customerDetail.CustomerDetailDTO;
import com.capstone.ads.dto.customerDetail.CustomerDetailRequest;
import com.capstone.ads.service.CustomerDetailService;
import com.capstone.ads.utils.ApiResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerDetailController {

    private final CustomerDetailService customerDetailService;

    @PostMapping(value = "/customer-details", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<CustomerDetailDTO> create(@RequestPart String companyName,
                                                 @RequestPart String tagLine,
                                                 @RequestPart String contactInfo,
                                                 @RequestPart MultipartFile customerDetailLogo) {
        return ApiResponseBuilder.buildSuccessResponse(
                "Create customer detail successful",
                customerDetailService.createCustomerDetail(companyName, tagLine, contactInfo, customerDetailLogo)
        );
    }

    @GetMapping("/customer-details/{customerDetailId}")
    public ApiResponse<CustomerDetailDTO> getById(@PathVariable("customerDetailId") String customerDetailId) {
        return ApiResponseBuilder.buildSuccessResponse(
                "Customer detail by ID",
                customerDetailService.getCustomerDetailById(customerDetailId)
        );
    }

    @GetMapping("/user/{userId}/customer-details")
    public ApiResponse<CustomerDetailDTO> getByUserId(@PathVariable String userId) {
        return ApiResponseBuilder.buildSuccessResponse(
                "Customer detail by user ID",
                customerDetailService.getCustomerDetailByUserId(userId)
        );
    }

    @GetMapping("/customer-details")
    public ApiResponse<List<CustomerDetailDTO>> getAll() {
        return ApiResponseBuilder.buildSuccessResponse(
                "Find all customer details",
                customerDetailService.getAllCustomerDetails()
        );
    }

    @PutMapping("/customer-details/{customerDetailId}")
    public ApiResponse<CustomerDetailDTO> updateCustomerDetailInformation(@Valid @PathVariable("customerDetailId") String customerDetailId,
                                                                          @RequestBody CustomerDetailRequest request) {
        return ApiResponseBuilder.buildSuccessResponse(
                "Update customer detail successful",
                customerDetailService.updateCustomerDetailInformation(customerDetailId, request)
        );
    }

    @PatchMapping(value = "/customer-details/{customerDetailId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<CustomerDetailDTO> updateCustomerDetailImage(@PathVariable("customerDetailId") String customerDetailId,
                                                                    @RequestPart MultipartFile image) {
        var response = customerDetailService.updateCustomerDetailLogoImage(customerDetailId, image);
        return ApiResponseBuilder.buildSuccessResponse("Update customer detail logo image successful", response);
    }

    @DeleteMapping("/customer-details/{customerDetailId}")
    public ApiResponse<Void> delete(@PathVariable("customerDetailId") String customerDetailId) {
        customerDetailService.deleteCustomerDetail(customerDetailId);
        return ApiResponseBuilder.buildSuccessResponse("Delete customer detail successful", null);
    }
}
