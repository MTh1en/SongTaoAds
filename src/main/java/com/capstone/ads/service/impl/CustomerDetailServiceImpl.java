package com.capstone.ads.service.impl;

import com.capstone.ads.dto.customerDetail.CustomerDetailDTO;
import com.capstone.ads.dto.customerDetail.CustomerDetailRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.CustomerDetailMapper;
import com.capstone.ads.model.CustomerDetail;
import com.capstone.ads.model.Users;
import com.capstone.ads.repository.internal.CustomerDetailRepository;
import com.capstone.ads.repository.internal.UsersRepository;
import com.capstone.ads.service.CustomerDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerDetailServiceImpl implements CustomerDetailService {
    private final CustomerDetailRepository customerDetailRepository;
    private final UsersRepository userRepository;
    private final CustomerDetailMapper customerDetailMapper;

    @Override
    public CustomerDetailDTO createCustomerDetail(CustomerDetailRequest request) {
        if (customerDetailRepository.existsByLogoUrl(request.getLogoUrl())) {
            throw new AppException(ErrorCode.LOGO_URL_ALREADY_EXISTS);
        }

        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        CustomerDetail customerDetail = customerDetailMapper.toEntity(request);
        customerDetail.setUsers(user);

        customerDetail = customerDetailRepository.save(customerDetail);
        return customerDetailMapper.toDTO(customerDetail);
    }

    @Override
    public CustomerDetailDTO getCustomerDetailById(String id) {
        CustomerDetail customerDetail = customerDetailRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_DETAIL_NOT_FOUND));
        return customerDetailMapper.toDTO(customerDetail);
    }

    @Override
    public CustomerDetailDTO getCustomerDetailByUserId(String id) {
        CustomerDetail customerDetail = customerDetailRepository.findByUsers_Id(id)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_DETAIL_NOT_FOUND));
        return customerDetailMapper.toDTO(customerDetail);
    }

    @Override
    public List<CustomerDetailDTO> getAllCustomerDetails() {
        return customerDetailRepository.findAll().stream()
                .map(customerDetailMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDetailDTO updateCustomerDetail(String id, CustomerDetailRequest request) {
        CustomerDetail customerDetail = customerDetailRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_DETAIL_NOT_FOUND));

        if (!customerDetail.getLogoUrl().equals(request.getLogoUrl()) &&
                customerDetailRepository.existsByLogoUrl(request.getLogoUrl())) {
            throw new AppException(ErrorCode.LOGO_URL_ALREADY_EXISTS);
        }

        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        customerDetailMapper.updateEntityFromDTO(request, customerDetail);
        customerDetail = customerDetailRepository.save(customerDetail);
        return customerDetailMapper.toDTO(customerDetail);
    }

    @Override
    public void deleteCustomerDetail(String id) {
        if (!customerDetailRepository.existsById(id)) {
            throw new AppException(ErrorCode.CUSTOMER_DETAIL_NOT_FOUND);
        }
        customerDetailRepository.deleteById(id);
    }
}