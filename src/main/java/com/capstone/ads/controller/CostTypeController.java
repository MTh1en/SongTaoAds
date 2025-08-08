package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.cost_type.CostTypeCreateRequest;
import com.capstone.ads.dto.cost_type.CostTypeDTO;
import com.capstone.ads.dto.cost_type.CostTypeUpdateRequest;
import com.capstone.ads.service.CostTypesService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "COST TYPE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CostTypeController {
    CostTypesService costTypesService;

    @PostMapping("/product-types/{productTypeId}/cost-types")
    @Operation(summary = "Tạo chi phí theo loại sản phẩm")
    public ApiResponse<CostTypeDTO> createCostType(@PathVariable String productTypeId,
                                                   @Valid @RequestBody CostTypeCreateRequest request) {
        var response = costTypesService.createCostTypeByProductType(productTypeId, request);
        return ApiResponseBuilder.buildSuccessResponse("Tạo loại chi phí thành công", response);
    }

    @PutMapping("/cost-types/{costTypeId}")
    @Operation(summary = "Cập nhật thông tin chi phí")
    public ApiResponse<CostTypeDTO> updateCostTypeInformation(@PathVariable String costTypeId,
                                                              @Valid @RequestBody CostTypeUpdateRequest request) {
        var response = costTypesService.updateCostTypeInformation(costTypeId, request);
        return ApiResponseBuilder.buildSuccessResponse("Cập nhật loại chi phí thành công", response);
    }

    @GetMapping("/product-types/{productTypeId}/cost-types")
    @Operation(summary = "Xem chi phí theo loại biển hiệu")
    public ApiResponse<List<CostTypeDTO>> findCostTypeByProductType(@PathVariable String productTypeId) {
        var response = costTypesService.findCostTypeByProductType(productTypeId);
        return ApiResponseBuilder.buildSuccessResponse("Xem chi phí theo loại biển hiệu thành công", response);
    }

    @GetMapping("/cost-types")
    @Operation(summary = "Xem tất cả chi phí")
    public ApiPagingResponse<CostTypeDTO> findAllCostTypes(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = costTypesService.findAllCostTypes(page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Xem tất cả các chi phí thành công", response, page);
    }

    @DeleteMapping("/cost-types/{costTypeId}")
    @Operation(summary = "Xóa cứng chi phí")
    public ApiResponse<Void> deleteCostType(@PathVariable String costTypeId) {
        costTypesService.hardDeleteCostType(costTypeId);
        return ApiResponseBuilder.buildSuccessResponse("Xóa chi phí thành công", null);
    }
}
