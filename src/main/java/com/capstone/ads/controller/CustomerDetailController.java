package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.customerDetail.CustomerDetailDTO;
import com.capstone.ads.dto.customerDetail.CustomerDetailRequestDTO;
import com.capstone.ads.service.CustomerDetailService;
import com.capstone.ads.utils.ApiResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer-details")
@RequiredArgsConstructor
public class CustomerDetailController {

    private final CustomerDetailService customerDetailService;

    @PostMapping
    public ResponseEntity<CustomerDetailDTO> createCustomerDetail(@Valid @RequestBody CustomerDetailRequestDTO request) {
        CustomerDetailDTO customerDetailDTO = customerDetailService.createCustomerDetail(request);
        return ResponseEntity.status(201).body(customerDetailDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDetailDTO> getCustomerDetailById(@PathVariable String id) {
        CustomerDetailDTO customerDetailDTO = customerDetailService.getCustomerDetailById(id);
        return ResponseEntity.ok(customerDetailDTO);
    }

    @GetMapping("/{userId}/user")
    public ResponseEntity<CustomerDetailDTO> getCustomerDetailByUserId(@PathVariable String userId) {
        CustomerDetailDTO customerDetailDTO = customerDetailService.getCustomerDetailByUserId(userId);
        return ResponseEntity.ok(customerDetailDTO);
    }

    @GetMapping
    public ResponseEntity<List<CustomerDetailDTO>> getAllCustomerDetails() {
        List<CustomerDetailDTO> customerDetails = customerDetailService.getAllCustomerDetails();
        return ResponseEntity.ok(customerDetails);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDetailDTO> updateCustomerDetail(@PathVariable String id, @Valid @RequestBody CustomerDetailRequestDTO request) {
        CustomerDetailDTO customerDetailDTO = customerDetailService.updateCustomerDetail(id, request);
        return ResponseEntity.ok(customerDetailDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerDetail(@PathVariable String id) {
        customerDetailService.deleteCustomerDetail(id);
        return ResponseEntity.noContent().build();
    }
}