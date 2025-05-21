package com.capstone.ads.service.impl;

import com.capstone.ads.dto.customerchoicedetail.CustomerChoicesDetailsDTO;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.CustomerChoicesDetailsMapper;
import com.capstone.ads.model.CustomerChoicesDetails;
import com.capstone.ads.repository.internal.AttributeValuesRepository;
import com.capstone.ads.repository.internal.CustomerChoicesDetailsRepository;
import com.capstone.ads.repository.internal.CustomerChoicesRepository;
import com.capstone.ads.service.CustomerChoicesDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerChoicesDetailsServiceImpl implements CustomerChoicesDetailsService {
    private final CustomerChoicesDetailsRepository customerChoicesDetailsRepository;
    private final CustomerChoicesRepository customerChoicesRepository;
    private final AttributeValuesRepository attributeValuesRepository;
    private final CustomerChoicesDetailsMapper customerChoicesDetailsMapper;

    @Override
    public CustomerChoicesDetailsDTO create(String customerChoicesId, String attributeValueId) {
        if (!customerChoicesRepository.existsById(customerChoicesId))
            throw new AppException(ErrorCode.CUSTOMER_CHOICES_NOT_FOUND);
        if (!attributeValuesRepository.existsById(attributeValueId))
            throw new AppException(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND);

        var customerChoicesDetails = customerChoicesDetailsMapper.toEntity(customerChoicesId, attributeValueId);
        customerChoicesDetails.setSubTotal(0.0);
        customerChoicesDetails = customerChoicesDetailsRepository.save(customerChoicesDetails);
        return customerChoicesDetailsMapper.toDTO(customerChoicesDetails);
    }

    @Override
    public CustomerChoicesDetailsDTO findById(String id) {
        CustomerChoicesDetails customerChoicesDetails = customerChoicesDetailsRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_DETAIL_NOT_FOUND));
        return customerChoicesDetailsMapper.toDTO(customerChoicesDetails);
    }

    @Override
    public List<CustomerChoicesDetailsDTO> findAllByCustomerChoicesId(String customerChoicesId) {
        if (!customerChoicesRepository.existsById(customerChoicesId))
            throw new AppException(ErrorCode.CUSTOMER_CHOICES_NOT_FOUND);
        return customerChoicesDetailsRepository.findByCustomerChoices_IdOrderByCreatedAtAsc(customerChoicesId).stream()
                .map(customerChoicesDetailsMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        if (!customerChoicesDetailsRepository.existsById(id)) {
            throw new AppException(ErrorCode.CUSTOMER_CHOICES_DETAIL_NOT_FOUND);
        }
        customerChoicesDetailsRepository.deleteById(id);
    }
}
