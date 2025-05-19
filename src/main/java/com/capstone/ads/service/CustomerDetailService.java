package com.capstone.ads.service;

import com.capstone.ads.dto.customerDetail.CustomerDetailDTO;
import com.capstone.ads.dto.customerDetail.CustomerDetailRequestDTO;

import java.util.List;

public interface CustomerDetailService {
    CustomerDetailDTO createCustomerDetail(CustomerDetailRequestDTO request);
    CustomerDetailDTO getCustomerDetailById(String id);
    List<CustomerDetailDTO> getAllCustomerDetails();
    CustomerDetailDTO updateCustomerDetail(String id, CustomerDetailRequestDTO request);
    void deleteCustomerDetail(String id);
}
