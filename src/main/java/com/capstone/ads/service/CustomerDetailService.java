package com.capstone.ads.service;

import com.capstone.ads.dto.customerDetail.CustomerDetailDTO;
import com.capstone.ads.dto.customerDetail.CustomerDetailRequest;

import java.util.List;

public interface CustomerDetailService {
    CustomerDetailDTO createCustomerDetail(CustomerDetailRequest request);
    CustomerDetailDTO getCustomerDetailById(String id);
    CustomerDetailDTO getCustomerDetailByUserId(String id);
    List<CustomerDetailDTO> getAllCustomerDetails();
    CustomerDetailDTO updateCustomerDetail(String id, CustomerDetailRequest request);
    void deleteCustomerDetail(String id);
}
