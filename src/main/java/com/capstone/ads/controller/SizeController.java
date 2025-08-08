package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.size.SizeCreateRequest;
import com.capstone.ads.dto.size.SizeDTO;
import com.capstone.ads.dto.size.SizeUpdateRequest;
import com.capstone.ads.service.SizeService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sizes")
@RequiredArgsConstructor
@Tag(name = "SIZE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SizeController {
    SizeService service;

    @PostMapping
    @Operation(summary = "Tạo kích thước")
    public ApiResponse<SizeDTO> createSize(@Valid @RequestBody SizeCreateRequest request) {
        var response = service.createSize(request);
        return ApiResponseBuilder.buildSuccessResponse("Tạo kích thước thành công", response);
    }

    @PutMapping("/{sizeId}")
    @Operation(summary = "Cập nhật kích thước")
    public ApiResponse<SizeDTO> updateSizeInformation(@PathVariable String sizeId,
                                                      @Valid @RequestBody SizeUpdateRequest request) {
        var response = service.updateSizeInformation(sizeId, request);
        return ApiResponseBuilder.buildSuccessResponse("Cập nhật kích thước thành công", response);
    }

    @GetMapping("/{sizeId}")
    @Operation(summary = "Xem kích thước theo ID")
    public ApiResponse<SizeDTO> findSizeById(@PathVariable String sizeId) {
        var response = service.findSizeById(sizeId);
        return ApiResponseBuilder.buildSuccessResponse("Xem kích thước theo ID thành công", response);
    }

    @GetMapping
    @Operation(summary = "Xem tất cả kích thước")
    public ApiPagingResponse<SizeDTO> findAllSize(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = service.findAllSize(page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Xem tất cả kích thước thành công", response, page);
    }

    @DeleteMapping("/{sizeId}")
    @Operation(summary = "Xóa cứng kích thước(Không dùng)")
    public ApiResponse<Void> hardDeleteSize(@PathVariable String sizeId) {
        service.hardDeleteSize(sizeId);
        return ApiResponseBuilder.buildSuccessResponse("Xóa cứng kích thước thành công", null);
    }
}
