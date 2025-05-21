package com.capstone.ads.service.impl;

import com.capstone.ads.dto.customerchoice.CustomerChoicesDTO;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.CustomerChoicesMapper;
import com.capstone.ads.model.CustomerChoices;
import com.capstone.ads.repository.internal.CustomerChoicesRepository;
import com.capstone.ads.repository.internal.ProductTypeRepository;
import com.capstone.ads.repository.internal.UsersRepository;
import com.capstone.ads.service.CustomerChoicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerChoicesServiceImpl implements CustomerChoicesService {
    private final CustomerChoicesRepository customerChoicesRepository;
    private final UsersRepository usersRepository;
    private final ProductTypeRepository productTypeRepository;
    private final CustomerChoicesMapper customerChoicesMapper;

    @Override
    @Transactional
    public CustomerChoicesDTO create(String customerId, String productTypeId) {
        if (!usersRepository.existsById(customerId)) throw new AppException(ErrorCode.USER_NOT_FOUND);
        if (!productTypeRepository.existsById(productTypeId)) throw new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND);

        CustomerChoices customerChoices = customerChoicesMapper.toEntity(customerId, productTypeId);
        customerChoices.setTotalAmount(0.0);
        customerChoices.setIsFinal(false);
        customerChoices = customerChoicesRepository.save(customerChoices);
        return customerChoicesMapper.toDTO(customerChoices);
    }

    @Override
    @Transactional
    public CustomerChoicesDTO finish(String customerId, String productTypeId) {
        var customerChoices = customerChoicesRepository.findFirstByProductType_IdAndUsers_IdOrderByUpdatedAtDesc(productTypeId, customerId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_NOT_FOUND));
        customerChoices.setIsFinal(true);
        customerChoices = customerChoicesRepository.save(customerChoices);
        return customerChoicesMapper.toDTO(customerChoices);
    }

    @Override
    public CustomerChoicesDTO findById(String customerChoiceId) {
        CustomerChoices customerChoices = customerChoicesRepository.findById(customerChoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_NOT_FOUND));
        return customerChoicesMapper.toDTO(customerChoices);
    }

    @Override
    public CustomerChoicesDTO findNewestByUserId(String userId) {
        usersRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        var customerChoicesDetail = customerChoicesRepository.findByUsers_IdOrderByUpdatedAtDesc(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_NOT_FOUND));
        return customerChoicesMapper.toDTO(customerChoicesDetail);
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (!customerChoicesRepository.existsById(id)) {
            throw new AppException(ErrorCode.CUSTOMER_CHOICES_NOT_FOUND);
        }
        customerChoicesRepository.deleteById(id);
    }
}
