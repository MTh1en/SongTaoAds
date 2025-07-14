package com.capstone.ads.service.impl;

import com.capstone.ads.dto.customer_detail.CustomerDetailCreateRequest;
import com.capstone.ads.dto.customer_detail.CustomerDetailDTO;
import com.capstone.ads.dto.customer_detail.CustomerDetailUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.CustomerDetailMapper;
import com.capstone.ads.model.CustomerDetail;
import com.capstone.ads.model.Users;
import com.capstone.ads.repository.internal.CustomerDetailRepository;
import com.capstone.ads.service.CustomerDetailService;
import com.capstone.ads.service.FileDataService;
import com.capstone.ads.utils.SecurityContextUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerDetailServiceImpl implements CustomerDetailService {
    FileDataService fileDataService;
    CustomerDetailRepository customerDetailRepository;
    CustomerDetailMapper customerDetailMapper;
    SecurityContextUtils securityContextUtils;

    @Override
    public CustomerDetailDTO createCustomerDetail(CustomerDetailCreateRequest request) {
        Users users = securityContextUtils.getCurrentUser();

        CustomerDetail customerDetail = customerDetailMapper.mapCreateRequestToEntity(request);
        String logoKeyAfterUploadToS3 = uploadCustomDesignImageToS3(users.getId(), request.getCustomerDetailLogo());
        customerDetail.setLogoUrl(logoKeyAfterUploadToS3);
        customerDetail.setUsers(users);

        customerDetail = customerDetailRepository.save(customerDetail);
        return customerDetailMapper.toDTO(customerDetail);
    }

    @Override
    public CustomerDetailDTO findCustomerDetailById(String customerDetailId) {
        CustomerDetail customerDetail = getCustomerChoiceDetailById(customerDetailId);
        return customerDetailMapper.toDTO(customerDetail);
    }

    @Override
    public CustomerDetailDTO findCustomerDetailByUserId(String userId) {
        CustomerDetail customerDetail = customerDetailRepository.findByUsers_Id(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_DETAIL_NOT_FOUND));
        return customerDetailMapper.toDTO(customerDetail);
    }

    @Override
    public List<CustomerDetailDTO> findAllCustomerDetails() {
        return customerDetailRepository.findAll().stream()
                .map(customerDetailMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDetailDTO updateCustomerDetailInformation(String customerDetailId, CustomerDetailUpdateRequest request) {
        CustomerDetail customerDetail = getCustomerChoiceDetailById(customerDetailId);

        customerDetailMapper.updateEntityFromDTO(request, customerDetail);
        customerDetail = customerDetailRepository.save(customerDetail);

        return customerDetailMapper.toDTO(customerDetail);
    }

    @Override
    public CustomerDetailDTO updateCustomerDetailLogoImage(String customerDetailId, MultipartFile logoImage) {
        CustomerDetail customerDetail = getCustomerChoiceDetailById(customerDetailId);

        String newImageLogoUrl = uploadCustomDesignImageToS3(customerDetailId, logoImage);
        customerDetail.setLogoUrl(newImageLogoUrl);
        customerDetail = customerDetailRepository.save(customerDetail);

        return customerDetailMapper.toDTO(customerDetail);
    }

    @Override
    public void hardDeleteCustomerDetail(String id) {
        if (!customerDetailRepository.existsById(id)) {
            throw new AppException(ErrorCode.CUSTOMER_DETAIL_NOT_FOUND);
        }
        customerDetailRepository.deleteById(id);
    }

    @Override
    public CustomerDetail getCustomerChoiceDetailById(String customerDetailId) {
        return customerDetailRepository.findById(customerDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_DETAIL_NOT_FOUND));
    }

    private String generateCustomerDetailImageKey(String userId) {
        return String.format("customer-detail/%s", userId);
    }

    private String uploadCustomDesignImageToS3(String userId, MultipartFile logo) {
        String customerDetailImageKey = generateCustomerDetailImageKey(userId);
        if (logo.isEmpty()) {
            throw new AppException(ErrorCode.FILE_REQUIRED);
        }
        fileDataService.uploadSingleFile(customerDetailImageKey, logo);
        return customerDetailImageKey;
    }
}