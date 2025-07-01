package com.capstone.ads.service;

import com.capstone.ads.dto.customer_detail.CustomerDetailCreateRequest;
import com.capstone.ads.dto.customer_detail.CustomerDetailDTO;
import com.capstone.ads.dto.customer_detail.CustomerDetailUpdateRequest;
import com.capstone.ads.model.CustomerDetail;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerDetailService {
    CustomerDetailDTO createCustomerDetail(CustomerDetailCreateRequest request);

    CustomerDetailDTO findCustomerDetailById(String customerDetailId);

    CustomerDetailDTO findCustomerDetailByUserId(String customerDetailId);

    List<CustomerDetailDTO> findAllCustomerDetails();

    CustomerDetailDTO updateCustomerDetailInformation(String customerDetailId, CustomerDetailUpdateRequest request);

    CustomerDetailDTO updateCustomerDetailLogoImage(String customerDetailId, MultipartFile logoImage);

    void hardDeleteCustomerDetail(String customerDetailId);

    //INTERNAL FUNCTION
    CustomerDetail getCustomerChoiceDetailById(String customerDetailId);
}
