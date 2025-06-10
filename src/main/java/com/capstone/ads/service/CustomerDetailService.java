package com.capstone.ads.service;

import com.capstone.ads.dto.customerDetail.CustomerDetailDTO;
import com.capstone.ads.dto.customerDetail.CustomerDetailRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerDetailService {
    CustomerDetailDTO createCustomerDetail(String companyName, String tagLine, String contactInfo, MultipartFile customerDetailLogo);

    CustomerDetailDTO getCustomerDetailById(String customerDetailId);

    CustomerDetailDTO getCustomerDetailByUserId(String customerDetailId);

    List<CustomerDetailDTO> getAllCustomerDetails();

    CustomerDetailDTO updateCustomerDetailInformation(String customerDetailId, CustomerDetailRequest request);

    CustomerDetailDTO updateCustomerDetailLogoImage(String customerDetailId, MultipartFile logoImage);

    void deleteCustomerDetail(String customerDetailId);
}
