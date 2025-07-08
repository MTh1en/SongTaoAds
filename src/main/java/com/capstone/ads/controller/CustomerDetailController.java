package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.customer_detail.CustomerDetailCreateRequest;
import com.capstone.ads.dto.customer_detail.CustomerDetailDTO;
import com.capstone.ads.dto.customer_detail.CustomerDetailUpdateRequest;
import com.capstone.ads.service.CustomerDetailService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "CUSTOMER DETAIL")
public class CustomerDetailController {

    private final CustomerDetailService customerDetailService;

    @PostMapping(value = "/customer-details", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Tạo thông tin doanh nghiệp")
    public ApiResponse<CustomerDetailDTO> createCustomerDetail(@Valid @ModelAttribute CustomerDetailCreateRequest request) {
        var response = customerDetailService.createCustomerDetail(request);
        return ApiResponseBuilder.buildSuccessResponse("Create customer detail successful", response);
    }

    @GetMapping("/customer-details/{customerDetailId}")
    @Operation(summary = "Xem thông tin doanh nghiệp theo ID")
    public ApiResponse<CustomerDetailDTO> getCustomerDetailById(@PathVariable("customerDetailId") String customerDetailId) {
        return ApiResponseBuilder.buildSuccessResponse(
                "Customer detail by ID",
                customerDetailService.findCustomerDetailById(customerDetailId)
        );
    }

    @GetMapping("/users/{userId}/customer-details")
    @Operation(summary = "Xem thông tin doanh nghiệp theo tài khoản")
    public ApiResponse<CustomerDetailDTO> getByUserId(@PathVariable String userId) {
        return ApiResponseBuilder.buildSuccessResponse(
                "Customer detail by user ID",
                customerDetailService.findCustomerDetailByUserId(userId)
        );
    }

    @GetMapping("/customer-details")
    @Operation(summary = "Xem tất cả thông tin doanh nghiệp")
    public ApiResponse<List<CustomerDetailDTO>> getAll() {
        return ApiResponseBuilder.buildSuccessResponse(
                "Find all customer details",
                customerDetailService.findAllCustomerDetails()
        );
    }

    @PatchMapping("/customer-details/{customerDetailId}/information")
    @Operation(summary = "Cập nhật thông tin doanh nghiệp")
    public ApiResponse<CustomerDetailDTO> updateCustomerDetailInformation(
            @PathVariable("customerDetailId") String customerDetailId,
            @Valid @RequestBody CustomerDetailUpdateRequest request) {
        return ApiResponseBuilder.buildSuccessResponse(
                "Update customer detail successful",
                customerDetailService.updateCustomerDetailInformation(customerDetailId, request)
        );
    }

    @PatchMapping(
            value = "/customer-details/{customerDetailId}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "Cập nhât logo doanh nghiệp")
    public ApiResponse<CustomerDetailDTO> updateCustomerDetailImage(@PathVariable("customerDetailId") String customerDetailId,
                                                                    @RequestPart MultipartFile image) {
        var response = customerDetailService.updateCustomerDetailLogoImage(customerDetailId, image);
        return ApiResponseBuilder.buildSuccessResponse("Update customer detail logo image successful", response);
    }

    @DeleteMapping("/customer-details/{customerDetailId}")
    @Operation(summary = "Xóa cứng thông tin doanh nghiệp")
    public ApiResponse<Void> delete(@PathVariable("customerDetailId") String customerDetailId) {
        customerDetailService.hardDeleteCustomerDetail(customerDetailId);
        return ApiResponseBuilder.buildSuccessResponse("Delete customer detail successful", null);
    }
}
