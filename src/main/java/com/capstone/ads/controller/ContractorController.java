package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.contractor.ContractorCreateRequest;
import com.capstone.ads.dto.contractor.ContractorDTO;
import com.capstone.ads.dto.contractor.ContractorUpdateRequest;
import com.capstone.ads.service.ContractorService;
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
@RequestMapping("/api/contractors")
@Tag(name = "CONTRACTOR")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContractorController {
    ContractorService contractorService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Tạo đơn vị thi công")
    public ApiResponse<ContractorDTO> createContractor(@Valid @ModelAttribute ContractorCreateRequest request) {
        var response = contractorService.createContractor(request);
        return ApiResponseBuilder.buildSuccessResponse("Create contractor successful", response);
    }

    @PutMapping("/{contractorId}")
    @Operation(summary = "Cập nhật thông tin đơn vị thi công")
    public ApiResponse<ContractorDTO> updateContractorInformation(@PathVariable String contractorId,
                                                                  @Valid @RequestBody ContractorUpdateRequest request) {
        var response = contractorService.updateContractorInformation(contractorId, request);
        return ApiResponseBuilder.buildSuccessResponse("Update contractor successful", response);
    }

    @PatchMapping(value = "/{contractorId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Cập nhật hình ảnh đơn vị thi công")
    public ApiResponse<ContractorDTO> updateContractorImage(@PathVariable String contractorId,
                                                            @RequestPart MultipartFile file) {
        var response = contractorService.updateContractorLogo(contractorId, file);
        return ApiResponseBuilder.buildSuccessResponse("Update contractor successful", response);
    }

    @GetMapping("/{contractorId}")
    @Operation(summary = "Xem đơn vị thi công theo ID")
    public ApiResponse<ContractorDTO> findContractorById(@PathVariable String contractorId) {
        var response = contractorService.findContractorById(contractorId);
        return ApiResponseBuilder.buildSuccessResponse("contractor by Id", response);
    }

    @GetMapping(params = "!isInternal")
    @Operation(summary = "Xem tất cả đơn vị thi công")
    public ApiPagingResponse<ContractorDTO> findAllContractors(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = contractorService.findAllContractors(page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Find all contractor", response, page);
    }

    @GetMapping(params = "isInternal")
    @Operation(summary = "Xem tất cả đơn vị thi công trong hoặc ngoài công ty")
    public ApiPagingResponse<ContractorDTO> findAllContractorByIsInternal(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(required = false) boolean isInternal) {
        var response = contractorService.findAllContractorByIsInternal(page, size, isInternal);
        return ApiResponseBuilder.buildPagingSuccessResponse("Find all contractor internal or external", response, page);
    }


    @DeleteMapping("/{contractorId}")
    @Operation(summary = "Xóa cứng kích thước(Không dùng)")
    public ApiResponse<Void> hardDeleteSize(@PathVariable String contractorId) {
        contractorService.hardDeleteContractor(contractorId);
        return ApiResponseBuilder.buildSuccessResponse("Delete contractor successful", null);
    }
}
