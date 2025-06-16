package com.capstone.ads.service.impl;

import com.capstone.ads.dto.customerchoicedetail.CustomerChoicesDetailsDTO;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.CustomerChoiceDetailsMapper;
import com.capstone.ads.model.AttributeValues;
import com.capstone.ads.model.CustomerChoiceDetails;
import com.capstone.ads.model.CustomerChoices;
import com.capstone.ads.repository.internal.AttributeValuesRepository;
import com.capstone.ads.repository.internal.CustomerChoiceDetailsRepository;
import com.capstone.ads.repository.internal.CustomerChoicesRepository;
import com.capstone.ads.service.CalculateService;
import com.capstone.ads.service.CustomerChoiceDetailsService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerChoiceDetailsServiceImpl implements CustomerChoiceDetailsService {
    private final CustomerChoiceDetailsRepository customerChoiceDetailsRepository;
    private final CustomerChoicesRepository customerChoicesRepository;
    private final AttributeValuesRepository attributeValuesRepository;
    private final CustomerChoiceDetailsMapper customerChoiceDetailsMapper;
    private final CalculateService calculateService;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public CustomerChoicesDetailsDTO createCustomerChoiceDetail(String customerChoicesId, String attributeValueId) {
        var customerChoices = customerChoicesRepository.findById(customerChoicesId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_NOT_FOUND));
        var attributeValues = attributeValuesRepository.findById(attributeValueId)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND));
        validateDuplicatedAttribute(customerChoices, attributeValues);
        validateAttributeNotBelongsToProductType(customerChoices, attributeValues);
        var customerChoicesDetails = CustomerChoiceDetails.builder()
                .customerChoices(customerChoices)
                .attributeValues(attributeValues)
                .build();
        customerChoicesDetails = customerChoiceDetailsRepository.save(customerChoicesDetails);
        updateSubtotalAndTotal(customerChoicesDetails);
        return customerChoiceDetailsMapper.toDTO(customerChoicesDetails);
    }

    @Override
    @Transactional
    public CustomerChoicesDetailsDTO updateAttributeValueInCustomerChoiceDetail(String customerChoiceDetailId, String attributeValueId) {
        var customerChoicesDetails = customerChoiceDetailsRepository.findById(customerChoiceDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_DETAIL_NOT_FOUND));
        var attributeValue = attributeValuesRepository.findById(attributeValueId)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND));

        validateAttributeBelongsToSameAttributeType(customerChoicesDetails.getAttributeValues(), attributeValue);

        customerChoicesDetails.setAttributeValues(attributeValue);
        customerChoicesDetails = customerChoiceDetailsRepository.save(customerChoicesDetails);

        updateSubtotalAndTotal(customerChoicesDetails);
        return customerChoiceDetailsMapper.toDTO(customerChoicesDetails);
    }

    @Override
    public CustomerChoicesDetailsDTO findCustomerChoiceDetailById(String id) {
        CustomerChoiceDetails customerChoiceDetails = customerChoiceDetailsRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_DETAIL_NOT_FOUND));
        return customerChoiceDetailsMapper.toDTO(customerChoiceDetails);
    }

    @Override
    public List<CustomerChoicesDetailsDTO> findAllCustomerChoiceDetailByCustomerChoicesId(String customerChoicesId) {
        if (!customerChoicesRepository.existsById(customerChoicesId))
            throw new AppException(ErrorCode.CUSTOMER_CHOICES_NOT_FOUND);
        return customerChoiceDetailsRepository.findByCustomerChoices_IdOrderByCreatedAtAsc(customerChoicesId).stream()
                .map(customerChoiceDetailsMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void hardDeleteCustomerChoiceDetail(String id) {
        if (!customerChoiceDetailsRepository.existsById(id)) {
            throw new AppException(ErrorCode.CUSTOMER_CHOICES_DETAIL_NOT_FOUND);
        }
        customerChoiceDetailsRepository.deleteById(id);
    }

    private void validateAttributeBelongsToSameAttributeType(AttributeValues currentValue, AttributeValues newValue) {
        if (!currentValue.getAttributes().getId().equals(newValue.getAttributes().getId())) {
            throw new AppException(ErrorCode.ATTRIBUTE_NOT_BELONG_CUSTOMER_CHOICE_DETAIL);
        }
    }

    private void validateDuplicatedAttribute(CustomerChoices customerChoices, AttributeValues attributeValues) {
        boolean isDuplicated = customerChoices.getCustomerChoiceDetails().stream()
                .anyMatch(detail ->
                        detail.getAttributeValues().getAttributes().getId()
                                .equals(attributeValues.getAttributes().getId())
                );
        if (isDuplicated) {
            throw new AppException(ErrorCode.ATTRIBUTE_EXISTED_IN_CUSTOMER_CHOICES_DETAIL);
        }
    }

    private void validateAttributeNotBelongsToProductType(CustomerChoices customerChoices, AttributeValues attributeValues) {
        boolean isBelong = customerChoices.getProductTypes().getAttributes().stream().anyMatch(detail ->
                detail.getId().equals(attributeValues.getAttributes().getId())
        );
        if (!isBelong) {
            throw new AppException(ErrorCode.ATTRIBUTE_NOT_BELONG_PRODUCT_TYPE);
        }
    }

    private void updateSubtotalAndTotal(CustomerChoiceDetails customerChoiceDetails) {
        // 1. Đảm bảo createdAt được thiết lập trước khi tính toán
        if (customerChoiceDetails.getCreatedAt() == null) {
            customerChoiceDetails.setCreatedAt(LocalDateTime.now());
        }

        // 2. Tính và cập nhật subtotal
        customerChoiceDetails.setSubTotal(calculateService.calculateSubtotal(customerChoiceDetails.getId()));
        customerChoiceDetails = customerChoiceDetailsRepository.saveAndFlush(customerChoiceDetails);

        // 3. Refresh toàn bộ đồ thị đối tượng liên quan
        entityManager.refresh(customerChoiceDetails); // Refresh chính details
        entityManager.refresh(customerChoiceDetails.getCustomerChoices()); // Refresh customerChoices

        // 4. Tính toán total với dữ liệu mới nhất
        String customerChoicesId = customerChoiceDetails.getCustomerChoices().getId();
        double total = calculateService.calculateTotal(customerChoicesId);

        // 5. Cập nhật total
        customerChoiceDetails.getCustomerChoices().setTotalAmount(total);
        customerChoiceDetailsRepository.saveAndFlush(customerChoiceDetails);

        log.info("Updated total for customerChoices {}: {}", customerChoicesId, total);
    }
}
