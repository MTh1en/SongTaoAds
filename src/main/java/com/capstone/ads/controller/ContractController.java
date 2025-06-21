package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.contract.ContractDTO;
import com.capstone.ads.service.ContractService;
import com.capstone.ads.utils.ApiResponseBuilder;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ContractController {
    private final ContractService contractService;

    @PostMapping(value = "/orders/{orderId}/contract", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ContractDTO> saleSendFirstContract(
            @PathVariable String orderId,
            @RequestParam(required = false, defaultValue = "10")
            @Range(min = 1, max = 100, message = "Percent between from 1 to 100") Long depositPercentChanged,
            @RequestPart String contractNumber,
            @RequestPart MultipartFile contactFile) {
        var response = contractService.saleSendFirstContract(orderId, contractNumber, depositPercentChanged, contactFile);
        return ApiResponseBuilder.buildSuccessResponse("Sale sent contract successfully", response);
    }

    @PatchMapping(value = "/contracts/{contractId}/signed-contact", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ContractDTO> customerSendSingedContract(@PathVariable String contractId,
                                                               @RequestPart MultipartFile signedContractFile) {
        var response = contractService.customerSendSingedContract(contractId, signedContractFile);
        return ApiResponseBuilder.buildSuccessResponse("Customer signed contract successfully", response);
    }

    @PatchMapping(value = "/contracts/{contractId}/revised-contract", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ContractDTO> saleSendRevisedContract(
            @PathVariable String contractId,
            @RequestParam(required = false, defaultValue = "10")
            @Range(min = 1, max = 100, message = "Percent between from 1 to 100") Long depositPercentChanged,
            @RequestPart MultipartFile contactFile) {
        var response = contractService.saleSendRevisedContract(contractId, depositPercentChanged, contactFile);
        return ApiResponseBuilder.buildSuccessResponse("Sale sent revised contract successfully", response);
    }

    @PatchMapping(value = "/contracts/{contractId}/discuss")
    public ApiResponse<ContractDTO> customerRequestDiscussForContract(@PathVariable String contractId) {
        var response = contractService.customerRequestDiscussForContract(contractId);
        return ApiResponseBuilder.buildSuccessResponse("Customer request discuss successfully", response);
    }

    @GetMapping("/orders/{orderId}/contract")
    public ApiResponse<ContractDTO> findContractByOrderId(@PathVariable String orderId) {
        var response = contractService.findContractByOrderId(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Find contract by order successfully", response);
    }
}
