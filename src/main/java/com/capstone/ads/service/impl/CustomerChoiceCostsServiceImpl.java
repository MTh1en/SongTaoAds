package com.capstone.ads.service.impl;

import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.model.*;
import com.capstone.ads.repository.internal.CustomerChoiceCostsRepository;
import com.capstone.ads.service.CustomerChoiceCostsService;
import com.capstone.ads.service.CustomerChoicesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerChoiceCostsServiceImpl implements CustomerChoiceCostsService {
    private final CustomerChoicesService customerChoicesService;
    private final CustomerChoiceCostsRepository customerChoiceCostsRepository;
    private final ExpressionParser parser = new SpelExpressionParser();

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
