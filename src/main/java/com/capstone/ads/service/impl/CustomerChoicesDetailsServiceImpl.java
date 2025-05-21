package com.capstone.ads.service.impl;

import com.capstone.ads.dto.customerchoicedetail.CustomerChoicesDetailsDTO;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.CustomerChoicesDetailsMapper;
import com.capstone.ads.model.AttributeValues;
import com.capstone.ads.model.CustomerChoices;
import com.capstone.ads.model.CustomerChoicesDetails;
import com.capstone.ads.repository.internal.AttributeValuesRepository;
import com.capstone.ads.repository.internal.CustomerChoicesDetailsRepository;
import com.capstone.ads.repository.internal.CustomerChoicesRepository;
import com.capstone.ads.service.CalculateService;
import com.capstone.ads.service.CustomerChoicesDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerChoicesDetailsServiceImpl implements CustomerChoicesDetailsService {
    private final CustomerChoicesDetailsRepository customerChoicesDetailsRepository;
    private final CustomerChoicesRepository customerChoicesRepository;
    private final AttributeValuesRepository attributeValuesRepository;
    private final CustomerChoicesDetailsMapper customerChoicesDetailsMapper;
    private final CalculateService calculateService;

    @Override
    @Transactional
    public CustomerChoicesDetailsDTO create(String customerChoicesId, String attributeValueId) {
        var customerChoices = customerChoicesRepository.findById(customerChoicesId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_NOT_FOUND));
        var attributeValues = attributeValuesRepository.findById(attributeValueId)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND));
        validateDuplicatedAttribute(customerChoices, attributeValues);
        var customerChoicesDetails = customerChoicesDetailsMapper.toEntity(customerChoicesId, attributeValueId);
        customerChoicesDetails.setSubTotal(0.0);
        customerChoicesDetails = customerChoicesDetailsRepository.save(customerChoicesDetails);
        return customerChoicesDetailsMapper.toDTO(customerChoicesDetails);
    }

    @Override
    @Transactional
    public CustomerChoicesDetailsDTO updateAttributeValue(String customerChoiceDetailId, String attributeValueId) {
        var customerChoicesDetails = customerChoicesDetailsRepository.findById(customerChoiceDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_DETAIL_NOT_FOUND));
        var attributeValue = attributeValuesRepository.findById(attributeValueId)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND));

        validateAttributeBelongsToSameAttributeType(customerChoicesDetails.getAttributeValues(), attributeValue);

        customerChoicesDetails.setAttributeValues(attributeValue);
        customerChoicesDetails = customerChoicesDetailsRepository.save(customerChoicesDetails);

        calculateService.calculateSubtotal(customerChoiceDetailId);
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
    @Transactional
    public void delete(String id) {
        if (!customerChoicesDetailsRepository.existsById(id)) {
            throw new AppException(ErrorCode.CUSTOMER_CHOICES_DETAIL_NOT_FOUND);
        }
        customerChoicesDetailsRepository.deleteById(id);
    }

    private void validateAttributeBelongsToSameAttributeType(AttributeValues currentValue, AttributeValues newValue) {
        if (!currentValue.getAttributes().getId().equals(newValue.getAttributes().getId())) {
            throw new AppException(ErrorCode.ATTRIBUTE_NOT_BELONG_CUSTOMER_CHOICE_DETAIL);
        }
    }

    private void validateDuplicatedAttribute(CustomerChoices customerChoices, AttributeValues attributeValues) {
        boolean isDuplicated = customerChoices.getCustomerChoicesDetails().stream()
                .anyMatch(detail ->
                        detail.getAttributeValues().getAttributes().getId()
                                .equals(attributeValues.getAttributes().getId())
                );
        if (isDuplicated) {
            throw new AppException(ErrorCode.ATTRIBUTE_EXISTED_IN_CUSTOMER_CHOICES_DETAIL);
        }
    }
}
