package com.capstone.ads.service.impl;

import com.capstone.ads.dto.customerchoice.CustomerChoicesCreateRequest;
import com.capstone.ads.dto.customerchoice.CustomerChoicesDTO;
import com.capstone.ads.dto.customerchoice.CustomerChoicesUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.CustomerChoicesMapper;
import com.capstone.ads.model.CustomerChoices;
import com.capstone.ads.repository.internal.CustomerChoicesRepository;
import com.capstone.ads.repository.internal.ProductTypeRepository;
import com.capstone.ads.repository.internal.UsersRepository;
import com.capstone.ads.service.CustomerChoicesService;
import com.capstone.ads.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerChoicesServiceImpl implements CustomerChoicesService {
    private final CustomerChoicesRepository customerChoicesRepository;
    private final UsersRepository usersRepository;
    private final ProductTypeRepository productTypeRepository;
    private final CustomerChoicesMapper customerChoicesMapper;
    private final SecurityContextUtils securityContextUtils;

    @Override
    @Transactional
    public CustomerChoicesDTO create(String productTypeId, CustomerChoicesCreateRequest request) {
        var user = securityContextUtils.getCurrentUser();
        productTypeRepository.findById(productTypeId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND));

        CustomerChoices customerChoices = customerChoicesMapper.toEntity(request, user.getId(), productTypeId);
        customerChoices = customerChoicesRepository.save(customerChoices);
        return customerChoicesMapper.toDTO(customerChoices);
    }

    @Override
    @Transactional
    public CustomerChoicesDTO update(String customerChoiceId, CustomerChoicesUpdateRequest request) {
        CustomerChoices customerChoices = customerChoicesRepository.findById(customerChoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_NOT_FOUND));
        if(customerChoices.getIsFinal()) throw new AppException(ErrorCode.CUSTOMER_CHOICES_IS_COMPLETED);
        customerChoicesMapper.updateEntityFromRequest(request, customerChoices);
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
    public List<CustomerChoicesDTO> findNewestByUserId(String userId) {
        usersRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return customerChoicesRepository.findByUsers_IdOrderByUpdatedAtDesc(userId).stream()
                .map(customerChoicesMapper::toDTO)
                .collect(Collectors.toList());
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
