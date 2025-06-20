package com.capstone.ads.service.impl;

import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.model.*;
import com.capstone.ads.repository.internal.CustomerChoiceDetailsRepository;
import com.capstone.ads.repository.internal.CustomerChoicesRepository;
import com.capstone.ads.service.CalculateService;
import jakarta.persistence.EntityManager;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalculateServiceImp implements CalculateService {
    private final ExpressionParser parser = new SpelExpressionParser();
    private final CustomerChoiceDetailsRepository customerChoiceDetailsRepository;
    private final CustomerChoicesRepository customerChoicesRepository;

    @Override
    @Transactional
    public Double calculateSubtotal(String customerChoicesDetailId) {
        CustomerChoiceDetails details = getValidatedCustomerChoicesDetails(customerChoicesDetailId);
        Attributes attribute = details.getAttributeValues().getAttributes();
        Map<String, Double> variables = prepareVariablesForSubtotal(details);
        Double subtotal = calculateWithFormula(attribute.getCalculateFormula(), variables);
        return subtotal;
//        log.info("Calculated subtotal: {}", subtotal);
//        details.setSubTotal(subtotal);
//        customerChoicesDetailsRepository.save(details);
    }

    @Override
    @Transactional
    public Double calculateTotal(String customerChoicesId) {
        CustomerChoices customerChoices = getValidatedCustomerChoices(customerChoicesId);
        ProductTypes productTypes = customerChoices.getProductTypes();
        String totalFormula = productTypes.getCalculateFormula().trim();

        // Khởi tạo biến với giá trị mặc định từ ProductType attributes
        Map<String, Double> variables = initializeVariables(productTypes);

        // Cập nhật biến từ CustomerChoicesSize (CAO, NGANG, KTCHỮVÀLOGO)
        updateSizeVariables(customerChoices, variables);

        // Cập nhật biến từ CustomerChoicesDetails (unitPrice hoặc giá trị tính toán)
        updateAttributeVariables(customerChoices, variables);

        Double total = calculateWithFormula(totalFormula, variables);
        customerChoices.setTotalAmount(total);
        customerChoicesRepository.save(customerChoices);
        log.info("Calculated total for customerChoicesId {}: {}", customerChoicesId, total);
        return total;
    }

    // Khởi tạo biến với giá trị mặc định 0.0 từ ProductType attributes
    private Map<String, Double> initializeVariables(ProductTypes productTypes) {
        List<String> productAttributes = getProductAttributes(productTypes);
        Map<String, Double> variables = new HashMap<>();
        productAttributes.forEach(attr -> variables.put(attr, 0.0));
        return variables;
    }

    // Cập nhật biến từ CustomerChoicesSize
    private void updateSizeVariables(CustomerChoices customerChoices, Map<String, Double> variables) {
        Map<String, Double> sizeValues = getSizeValues(customerChoices);
        variables.putAll(sizeValues);
    }

    // Cập nhật biến từ CustomerChoicesDetails
    private void updateAttributeVariables(CustomerChoices customerChoices, Map<String, Double> variables) {
        customerChoices.getCustomerChoiceDetails().forEach(detail -> {
            String attributeName = normalizeName(detail.getAttributeValues().getAttributes().getName());
            Double value = detail.getAttributeValues().getUnitPrice();
            variables.put(attributeName, value);
        });
    }

    // Lấy danh sách tên thuộc tính từ ProductType
    private List<String> getProductAttributes(ProductTypes productTypes) {
        return Optional.ofNullable(productTypes.getAttributes())
                .orElse(List.of())
                .stream()
                .map(Attributes::getName)
                .map(this::normalizeName)
                .distinct()
                .collect(Collectors.toList());
    }

    // Chuẩn bị biến cho tính toán subtotal
    private Map<String, Double> prepareVariablesForSubtotal(CustomerChoiceDetails customerChoiceDetails) {
        Map<String, Double> variables = new HashMap<>();
        variables.put("unitPrice", Optional.ofNullable(customerChoiceDetails.getAttributeValues())
                .map(AttributeValues::getUnitPrice)
                .orElse(0.0));
        variables.putAll(getSizeValues(customerChoiceDetails.getCustomerChoices()));
        return variables;
    }

    // Tính toán giá trị từ công thức SpEL
    private Double calculateWithFormula(String formula, Map<String, Double> variables) {
        if (formula == null || formula.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_FORMULA);
        }

        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(new HashMap<>(variables));
        Expression expression = parser.parseExpression(formula);
        Double result = expression.getValue(context, Double.class);

        if (result == null) {
            throw new AppException(ErrorCode.CALCULATION_FAILED);
        }
        return result;
    }

    // Lấy danh sách kích thước từ CustomerChoices
    private Map<String, Double> getSizeValues(CustomerChoices customerChoices) {
        return Optional.ofNullable(customerChoices.getCustomerChoiceSizes())
                .orElse(List.of())
                .stream()
                .collect(Collectors.toMap(
                        mapping -> normalizeName(mapping.getSizes().getName()),
                        CustomerChoiceSizes::getSizeValue,
                        (v1, v2) -> v1
                ));
    }

    // Xác thực CustomerChoicesDetails
    private CustomerChoiceDetails getValidatedCustomerChoicesDetails(String customerChoicesDetailId) {
        CustomerChoiceDetails details = customerChoiceDetailsRepository.findById(customerChoicesDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_DETAIL_NOT_FOUND));

        if (details.getAttributeValues() == null || details.getAttributeValues().getAttributes() == null) {
            throw new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND);
        }
        return details;
    }

    // Xác thực CustomerChoices
    private CustomerChoices getValidatedCustomerChoices(String customerChoicesId) {
        CustomerChoices customerChoices = customerChoicesRepository.findById(customerChoicesId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_NOT_FOUND));

        if (customerChoices.getProductTypes() == null || customerChoices.getProductTypes().getCalculateFormula() == null) {
            throw new AppException(ErrorCode.INVALID_FORMULA);
        }
//        if (customerChoices.getCustomerChoicesDetails() == null || customerChoices.getCustomerChoicesDetails().isEmpty()) {
//            throw new AppException(ErrorCode.CUSTOMER_CHOICES_DETAIL_NOT_FOUND);
//        }
        if (customerChoices.getCustomerChoiceSizes() == null || customerChoices.getCustomerChoiceSizes().isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_TYPE_SIZE_NOT_FOUND);
        }
        return customerChoices;
    }

    // Chuẩn hóa tên (loại bỏ khoảng trắng)
    private String normalizeName(String name) {
        return name.trim().replaceAll("\\s+", "");
    }
}
