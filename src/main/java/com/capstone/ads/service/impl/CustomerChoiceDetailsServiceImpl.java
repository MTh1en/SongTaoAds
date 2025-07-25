package com.capstone.ads.service.impl;

import com.capstone.ads.dto.customer_choice_detail.CustomerChoicesDetailsDTO;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.CustomerChoiceDetailsMapper;
import com.capstone.ads.model.*;
import com.capstone.ads.repository.internal.CustomerChoiceDetailsRepository;
import com.capstone.ads.service.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerChoiceDetailsServiceImpl implements CustomerChoiceDetailsService {
    CustomerChoicesService customerChoicesService;
    CustomerChoiceCostsService customerChoiceCostsService;
    AttributeValuesService attributeValuesService;
    CustomerChoiceDetailsRepository customerChoiceDetailsRepository;
    CustomerChoiceDetailsMapper customerChoiceDetailsMapper;
    ExpressionParser parser = new SpelExpressionParser();

    @Override
    @Transactional
    public CustomerChoicesDetailsDTO createCustomerChoiceDetail(String customerChoicesId, String attributeValueId) {
        var customerChoices = customerChoicesService.getCustomerChoiceById(customerChoicesId);
        var attributeValues = attributeValuesService.getAttributeValueById(attributeValueId);

        validateDuplicatedAttribute(customerChoices, attributeValues);
        validateAttributeNotBelongsToProductType(customerChoices, attributeValues);
        var customerChoicesDetails = CustomerChoiceDetails.builder()
                .createdAt(LocalDateTime.now())
                .customerChoices(customerChoices)
                .attributeValues(attributeValues)
                .build();

        customerChoicesDetails = customerChoiceDetailsRepository.save(customerChoicesDetails);
        customerChoices.getCustomerChoiceDetails().add(customerChoicesDetails);

        recalculateSubtotal(customerChoicesDetails);
        customerChoiceCostsService.calculateAllCosts(customerChoices);
        customerChoicesService.recalculateTotalAmount(customerChoices);
        return customerChoiceDetailsMapper.toDTO(customerChoicesDetails);
    }

    @Override
    @Transactional
    public CustomerChoicesDetailsDTO updateAttributeValueInCustomerChoiceDetail(String customerChoiceDetailId, String attributeValueId) {
        var customerChoicesDetails = customerChoiceDetailsRepository.findById(customerChoiceDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_DETAIL_NOT_FOUND));
        var attributeValue = attributeValuesService.getAttributeValueById(attributeValueId);

        validateAttributeBelongsToSameAttributeType(customerChoicesDetails.getAttributeValues(), attributeValue);

        customerChoicesDetails.setAttributeValues(attributeValue);
        customerChoicesDetails = customerChoiceDetailsRepository.save(customerChoicesDetails);

        recalculateSubtotal(customerChoicesDetails);
        customerChoiceCostsService.calculateAllCosts(customerChoicesDetails.getCustomerChoices());
        customerChoicesService.recalculateTotalAmount(customerChoicesDetails.getCustomerChoices());
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
        customerChoicesService.validateCustomerChoiceExists(customerChoicesId);

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

    @Override
    public void recalculateSubtotal(CustomerChoiceDetails customerChoiceDetails) {
        long newValue = calculateSubtotal(customerChoiceDetails);
        customerChoiceDetails.setSubTotal(newValue);
        customerChoiceDetailsRepository.save(customerChoiceDetails);
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

    //CALCULATE SUBTOTAL

    public Long calculateSubtotal(CustomerChoiceDetails customerChoicesDetail) {
        Attributes attribute = customerChoicesDetail.getAttributeValues().getAttributes();

        Map<String, Float> variables = prepareVariablesForSubtotal(
                customerChoicesDetail.getAttributeValues(),
                customerChoicesDetail.getCustomerChoices().getCustomerChoiceSizes()
        );

        return calculateWithFormula(attribute.getCalculateFormula(), variables);
    }

    private Map<String, Float> prepareVariablesForSubtotal(AttributeValues attributeValues, List<CustomerChoiceSizes> customerChoiceSizes) {
        Map<String, Float> variables = new HashMap<>();

        variables.put("unitPrice", Optional.ofNullable(attributeValues)
                .map(AttributeValues::getUnitPrice)
                .orElse(0L).floatValue());

        variables.putAll(getSizeValues(customerChoiceSizes));
        return variables;
    }

    private Long calculateWithFormula(String formula, Map<String, Float> variables) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(new HashMap<>(variables));
        Expression expression = parser.parseExpression(formula);
        return expression.getValue(context, Long.class);
    }

    private Map<String, Float> getSizeValues(List<CustomerChoiceSizes> customerChoiceSizes) {
        Map<String, Float> variables = new HashMap<>();
        customerChoiceSizes.forEach(size -> {
            String sizeName = size.getSizes().getName();
            variables.put(normalizeName(sizeName), size.getSizeValue());
        });
        return variables;
    }

    // Chuẩn hóa tên (loại bỏ khoảng trắng)
    private String normalizeName(String name) {
        return name.trim().replaceAll("\\s+", "");
    }
}
