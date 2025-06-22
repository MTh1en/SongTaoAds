package com.capstone.ads.service.impl;

import com.capstone.ads.dto.customer_choice.CustomerChoicesDTO;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.CustomerChoicesMapper;
import com.capstone.ads.model.*;
import com.capstone.ads.repository.internal.CustomerChoicesRepository;
import com.capstone.ads.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerChoicesServiceImpl implements CustomerChoicesService {
    private final UserService userService;
    private final ProductTypesService productTypesService;
    private final CustomerChoicesRepository customerChoicesRepository;
    private final CustomerChoicesMapper customerChoicesMapper;
    private final ExpressionParser parser = new SpelExpressionParser();

    @Override
    @Transactional
    public CustomerChoicesDTO createCustomerChoice(String customerId, String productTypeId) {
        userService.validateUserExistsAndIsActive(customerId);
        productTypesService.validateProductTypeExistsAndAvailable(productTypeId);

        CustomerChoices customerChoices = customerChoicesMapper.toEntity(customerId, productTypeId);
        customerChoices.setTotalAmount(0L);
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
    public CustomerChoices getCustomerChoiceById(String customerChoiceId) {
        return customerChoicesRepository.findById(customerChoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_NOT_FOUND));
    }

    @Override
    public void validateCustomerChoiceExists(String customerChoiceId) {
        if (!customerChoicesRepository.existsById(customerChoiceId))
            throw new AppException(ErrorCode.CUSTOMER_CHOICES_NOT_FOUND);
    }

    @Override
    @Transactional
    public void recalculateTotalAmount(CustomerChoices customerChoices) {
        long newValue = calculateTotal(customerChoices);
        customerChoices.setTotalAmount(newValue);
        customerChoicesRepository.save(customerChoices);
    }

    //CALCULATE TOTAL
    public Long calculateTotal(CustomerChoices customerChoices) {
        ProductTypes productTypes = customerChoices.getProductTypes();
        String totalFormula = productTypes.getCalculateFormula().trim();
        List<Attributes> attributes = productTypes.getAttributes();

        Map<String, Float> variables = new HashMap<>();
        initializeVariables(attributes, variables);
        updateSizeVariables(customerChoices.getCustomerChoiceSizes(), variables);
        updateAttributeVariables(customerChoices.getCustomerChoiceDetails(), variables);

        return calculateWithFormula(totalFormula, variables);
    }

    // Khởi tạo biến với giá trị mặc định 0.0 từ ProductType attributes
    private void initializeVariables(List<Attributes> attributes, Map<String, Float> variables) {
        List<String> productAttributes = attributes.stream()
                .map(a -> normalizeName(a.getName()))
                .toList();
        productAttributes.forEach(attr -> variables.put(attr, 0F));
    }

    private void updateSizeVariables(List<CustomerChoiceSizes> customerChoiceSizes, Map<String, Float> variables) {
        customerChoiceSizes.forEach(size -> {
            String sizeName = size.getSizes().getName();
            variables.put(normalizeName(sizeName), size.getSizeValue());
        });

    }

    // Cập nhật biến từ CustomerChoicesDetails
    private void updateAttributeVariables(List<CustomerChoiceDetails> customerChoiceDetails, Map<String, Float> variables) {
        customerChoiceDetails.forEach(detail -> {
            String attributeName = normalizeName(detail.getAttributeValues().getAttributes().getName());
            Float value = (detail.getAttributeValues().getIsMultiplier())
                    ? detail.getAttributeValues().getUnitPrice() / 10
                    : detail.getAttributeValues().getUnitPrice().floatValue();
            variables.put(attributeName, value);
        });
    }

    // Tính toán giá trị từ công thức SpEL
    private Long calculateWithFormula(String formula, Map<String, Float> variables) {
        if (formula == null || formula.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_FORMULA);
        }

        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(new HashMap<>(variables));
        Expression expression = parser.parseExpression(formula);
        Long result = expression.getValue(context, Long.class);

        if (result == null) {
            throw new AppException(ErrorCode.CALCULATION_FAILED);
        }
        return result;
    }

    private String normalizeName(String name) {
        return name.trim().replaceAll("\\s+", "");
    }
}
