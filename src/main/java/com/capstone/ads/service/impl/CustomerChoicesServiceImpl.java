package com.capstone.ads.service.impl;

import com.capstone.ads.dto.customer_choice.CustomerChoicesDTO;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.CustomerChoicesMapper;
import com.capstone.ads.model.CustomerChoices;
import com.capstone.ads.repository.internal.CustomerChoicesRepository;
import com.capstone.ads.service.CustomerChoicesService;
import com.capstone.ads.service.ProductTypesService;
import com.capstone.ads.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerChoicesServiceImpl implements CustomerChoicesService {
    private final UserService userService;
    private final ProductTypesService productTypesService;
    private final CustomerChoicesRepository customerChoicesRepository;
    private final CustomerChoicesMapper customerChoicesMapper;

    @Override
    @Transactional
    public CustomerChoicesDTO createCustomerChoice(String customerId, String productTypeId) {
        userService.validateUserExistsAndIsActive(customerId);
        productTypesService.validateProductTypeExistsAndAvailable(productTypeId);

        CustomerChoices customerChoices = customerChoicesMapper.toEntity(customerId, productTypeId);
        customerChoices.setTotalAmount(0.0);
        customerChoices = customerChoicesRepository.save(customerChoices);
        return customerChoicesMapper.toDTO(customerChoices);
    }

    @Override
    public CustomerChoicesDTO findCustomerChoiceById(String customerChoiceId) {
        CustomerChoices customerChoices = customerChoicesRepository.findById(customerChoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_NOT_FOUND));
        return customerChoicesMapper.toDTO(customerChoices);
    }

    @Override
    public CustomerChoicesDTO findCustomerChoiceByUserId(String userId) {
        userService.validateUserExistsAndIsActive(userId);

        var customerChoicesDetail = customerChoicesRepository.findByUsers_IdOrderByUpdatedAtDesc(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_NOT_FOUND));
        return customerChoicesMapper.toDTO(customerChoicesDetail);
    }

    @Override
    @Transactional
    public void hardDeleteCustomerChoice(String id) {
        if (!customerChoicesRepository.existsById(id)) {
            throw new AppException(ErrorCode.CUSTOMER_CHOICES_NOT_FOUND);
        }
        customerChoicesRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CustomerChoices getCustomerChoiceById(String customerChoiceId) {
        return customerChoicesRepository.findById(customerChoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_NOT_FOUND));
    }
}
