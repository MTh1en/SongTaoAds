package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.S3ImageDuration;
import com.capstone.ads.dto.customer_detail.CustomerDetailDTO;
import com.capstone.ads.dto.customer_detail.CustomerDetailRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.CustomerDetailMapper;
import com.capstone.ads.model.CustomerDetail;
import com.capstone.ads.model.Users;
import com.capstone.ads.repository.internal.CustomerDetailRepository;
import com.capstone.ads.service.CustomerDetailService;
import com.capstone.ads.service.S3Service;
import com.capstone.ads.service.UserService;
import com.capstone.ads.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerDetailServiceImpl implements CustomerDetailService {
    private final UserService userService;
    private final S3Service s3Service;
    private final CustomerDetailRepository customerDetailRepository;
    private final CustomerDetailMapper customerDetailMapper;
    private final SecurityContextUtils securityContextUtils;

    @Override
    public CustomerDetailDTO createCustomerDetail(String companyName, String tagLine, String contactInfo, MultipartFile customerDetailLogo) {
        Users users = securityContextUtils.getCurrentUser();
        userService.validateUserExistsAndIsActive(users.getId());

        CustomerDetail customerDetail = customerDetailMapper.toEntity(companyName, tagLine, contactInfo);
        String logoKeyAfterUploadToS3 = uploadCustomDesignImageToS3(users.getId(), customerDetailLogo);
        customerDetail.setLogoUrl(logoKeyAfterUploadToS3);
        customerDetail.setUsers(users);

        customerDetail = customerDetailRepository.save(customerDetail);
        return customerDetailMapper.toDTO(customerDetail);
    }

    @Override
    public CustomerDetailDTO findCustomerDetailById(String customerDetailId) {
        CustomerDetail customerDetail = findById(customerDetailId);
        return convertToCustomerDetailDTOWithLogoUrlIsPresignedURL(customerDetail);
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
                .map(this::convertToCustomerDetailDTOWithLogoUrlIsPresignedURL)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDetailDTO updateCustomerDetailInformation(String customerDetailId, CustomerDetailRequest request) {
        CustomerDetail customerDetail = findById(customerDetailId);
        customerDetailMapper.updateEntityFromDTO(request, customerDetail);
        customerDetail = customerDetailRepository.save(customerDetail);
        return customerDetailMapper.toDTO(customerDetail);
    }

    @Override
    public CustomerDetailDTO updateCustomerDetailLogoImage(String customerDetailId, MultipartFile logoImage) {
        CustomerDetail customerDetail = findById(customerDetailId);
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
    @Transactional(readOnly = true)
    public void validateCustomerDetailExists(String customerDetailId) {
        if (!customerDetailRepository.existsById(customerDetailId)) {
            throw new AppException(ErrorCode.CUSTOMER_DETAIL_NOT_FOUND);
        }
    }

    private CustomerDetail findById(String customerDetailId) {
        return customerDetailRepository.findById(customerDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_DETAIL_NOT_FOUND));
    }

    private String generateCustomerDetailImageKey(String userId) {
        return String.format("customer-detail/%s/%s", userId, UUID.randomUUID());
    }

    private String uploadCustomDesignImageToS3(String userId, MultipartFile logo) {
        String customerDetailImageKey = generateCustomerDetailImageKey(userId);
        if (logo.isEmpty()) {
            throw new AppException(ErrorCode.FILE_REQUIRED);
        }
        s3Service.uploadSingleFile(customerDetailImageKey, logo);
        return customerDetailImageKey;
    }

    private CustomerDetailDTO convertToCustomerDetailDTOWithLogoUrlIsPresignedURL(CustomerDetail customerDetail) {
        var customerDetailDTOResponse = customerDetailMapper.toDTO(customerDetail);
        if (!Objects.isNull(customerDetail.getLogoUrl())) {
            var designTemplateImagePresigned = s3Service.getPresignedUrl(customerDetail.getLogoUrl(), S3ImageDuration.CUSTOM_DESIGN_DURATION);
            customerDetailDTOResponse.setLogoUrl(designTemplateImagePresigned);
        }
        return customerDetailDTOResponse;
    }
}