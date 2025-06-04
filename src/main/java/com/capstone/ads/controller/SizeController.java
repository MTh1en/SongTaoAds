package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.size.SizeCreateRequest;
import com.capstone.ads.dto.size.SizeDTO;
import com.capstone.ads.dto.size.SizeUpdateRequest;
import com.capstone.ads.service.SizeService;
import com.capstone.ads.utils.ApiResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sizes")
@RequiredArgsConstructor
public class SizeController {
    private final SizeService service;

    @PostMapping
    public ApiResponse<SizeDTO> create(@Valid @RequestBody SizeCreateRequest request) {
        var response = service.create(request);
        return ApiResponseBuilder.buildSuccessResponse("Create size successful", response);
    }

    @PutMapping("/{sizeId}")
    public ApiResponse<SizeDTO> update(@Valid @PathVariable String sizeId,
                                       @RequestBody SizeUpdateRequest request) {
        var response = service.update(sizeId, request);
        return ApiResponseBuilder.buildSuccessResponse("Update size successful", response);
    }

    @GetMapping("/{sizeId}")
    public ApiResponse<SizeDTO> getById(@PathVariable String sizeId) {
        var response = service.findById(sizeId);
        return ApiResponseBuilder.buildSuccessResponse("size by Id", response);
    }

    @GetMapping
    public ApiResponse<List<SizeDTO>> getAll() {
        var response = service.findAll();
        return ApiResponseBuilder.buildSuccessResponse("Find all size", response);
    }

    @DeleteMapping("/{sizeId}")
    public ApiResponse<Void> delete(@PathVariable String sizeId) {
        service.delete(sizeId);
        return ApiResponseBuilder.buildSuccessResponse("Delete size successful", null);
    }
}
