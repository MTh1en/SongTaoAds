package com.capstone.ads.service;

import com.capstone.ads.dto.customerDetail.CustomerDetailDTO;
import com.capstone.ads.dto.customerDetail.CustomerDetailRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerDetailService {
    CustomerDetailDTO createCustomerDetail(String companyName, String tagLine, String contactInfo, MultipartFile customerDetailLogo);

    CustomerDetailDTO findCustomerDetailById(String customerDetailId);

    CustomerDetailDTO findCustomerDetailByUserId(String customerDetailId);

    List<CustomerDetailDTO> findAllCustomerDetails();

    CustomerDetailDTO updateCustomerDetailInformation(String customerDetailId, CustomerDetailRequest request);

    CustomerDetailDTO updateCustomerDetailLogoImage(String customerDetailId, MultipartFile logoImage);

    void hardDeleteCustomerDetail(String customerDetailId);
}
