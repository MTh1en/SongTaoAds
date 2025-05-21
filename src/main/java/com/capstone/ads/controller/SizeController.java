package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.size.SizeCreateRequest;
import com.capstone.ads.dto.size.SizeDTO;
import com.capstone.ads.dto.size.SizeUpdateRequest;
import com.capstone.ads.service.SizeService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sizes")
@RequiredArgsConstructor
public class SizeController {
    private final SizeService service;

    @PostMapping
    public ApiResponse<SizeDTO> create(@RequestBody SizeCreateRequest request) {
        return ApiResponseBuilder.buildSuccessResponse("Create size successful", service.create(request));
    }

    @PutMapping("/{sizeId}")
    public ApiResponse<SizeDTO> update(@PathVariable String sizeId,
                                          @RequestBody SizeUpdateRequest request) {
        return ApiResponseBuilder.buildSuccessResponse("Update size successful", service.update(sizeId, request));
    }

    @GetMapping("/{sizeId}")
    public ApiResponse<SizeDTO> getById(@PathVariable String sizeId) {
        return ApiResponseBuilder.buildSuccessResponse("size by Id", service.findById(sizeId));
    }

    @GetMapping
    public ApiResponse<List<SizeDTO>> getAll() {
        return ApiResponseBuilder.buildSuccessResponse("Find all size", service.findAll());
    }

    @DeleteMapping("/{sizeId}")
    public ApiResponse<Void> delete(@PathVariable String sizeId) {
        service.delete(sizeId);
        return ApiResponseBuilder.buildSuccessResponse("Delete size successful", null);
    }
}
