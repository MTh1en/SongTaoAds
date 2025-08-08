package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.contract.ContractDTO;
import com.capstone.ads.dto.contract.ContractRevisedRequest;
import com.capstone.ads.dto.contract.ContractSendRequest;
import com.capstone.ads.service.ContractService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "CONTRACT")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContractController {
    ContractService contractService;

    @PostMapping(value = "/orders/{orderId}/contract", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Sale tạo bản hợp đồng đầu tiên")
    public ApiResponse<ContractDTO> saleSendFirstContract(
            @PathVariable String orderId,
            @Valid @ModelAttribute ContractSendRequest request) {
        var response = contractService.saleSendFirstContract(orderId, request);
        return ApiResponseBuilder.buildSuccessResponse("Sale gửi hợp đồng thành công", response);
    }

    @PatchMapping(value = "/contracts/{contractId}/signed-contract", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Khách hàng gửi hợp đồng đã ký")
    public ApiResponse<ContractDTO> customerSendSingedContract(@PathVariable String contractId,
                                                               @RequestPart MultipartFile signedContractFile) {
        var response = contractService.customerSendSingedContract(contractId, signedContractFile);
        return ApiResponseBuilder.buildSuccessResponse("Khách hàng gửi hợp đồng đã ký thành công", response);
    }

    @PatchMapping(value = "/contracts/{contractId}/revised-contract", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Sale gửi lại bản hợp đồng đã chỉnh sửa")
    public ApiResponse<ContractDTO> saleSendRevisedContract(
            @PathVariable String contractId,
            @Valid @ModelAttribute ContractRevisedRequest request) {
        var response = contractService.saleSendRevisedContract(contractId, request);
        return ApiResponseBuilder.buildSuccessResponse("Sale gửi lại hợp đồng đã ký thành công", response);
    }

    @PatchMapping(value = "/contracts/{contractId}/discuss")
    @Operation(summary = "Khách hàng yêu cầu thảo luận thêm về hợp đồng")
    public ApiResponse<ContractDTO> customerRequestDiscussForContract(@PathVariable String contractId) {
        var response = contractService.customerRequestDiscussForContract(contractId);
        return ApiResponseBuilder.buildSuccessResponse("Khách hàng yêu cầu bàn luận về hợp đồng thành công", response);
    }

    @GetMapping("/orders/{orderId}/contract")
    @Operation(summary = "Xem hợp đồng theo đơn hàng")
    public ApiResponse<ContractDTO> findContractByOrderId(@PathVariable String orderId) {
        var response = contractService.findContractByOrderId(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Xem hợp đồng theo đơn hàng", response);
    }
}
