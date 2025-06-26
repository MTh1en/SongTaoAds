package com.capstone.ads.service.impl;

import com.capstone.ads.dto.customer_choice_cost.CustomerChoiceCostDTO;
import com.capstone.ads.mapper.CustomerChoiceCostMapper;
import com.capstone.ads.model.*;
import com.capstone.ads.repository.internal.CustomerChoiceCostsRepository;
import com.capstone.ads.service.CostTypesService;
import com.capstone.ads.service.CustomerChoiceCostsService;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerChoiceCostsServiceImpl implements CustomerChoiceCostsService {
    private final CostTypesService costTypesService;
    private final CustomerChoiceCostMapper customerChoiceCostMapper;
    private final CustomerChoiceCostsRepository customerChoiceCostsRepository;
    private final ExpressionParser parser = new SpelExpressionParser();


    @Override
    public List<CustomerChoiceCostDTO> findCustomerChoiceCostByCustomerChoice(String customerChoiceId) {
        return getCustomerChoiceCostByCustomerChoiceId(customerChoiceId).stream()
                .map(customerChoiceCostMapper::toDTO)
                .toList();
    }

    //CALCULATE Material
    @Override
    @Transactional
    public void calculateAllCosts(CustomerChoices customerChoice) {
        // 1. Tạo context chứa biến cơ bản
        Map<String, Object> context = createBaseContext(customerChoice);
        String productTypeId = customerChoice.getProductTypes().getId();

        // 2. Tính toán chi phí core trước
        CostTypes coreCostType = costTypesService.getCoreCostTypeByProductType(productTypeId);
        Long coreCostValue = evaluateFormula(coreCostType.getFormula(), context);
        context.put(normalizeName(coreCostType.getName()), coreCostValue.doubleValue());

        // 3. Tính toán các chi phí khác không phải chi phí core theo mức độ ưu tiên
        Map<CostTypes, Long> calculatedCosts = new HashMap<>();
        calculatedCosts.put(coreCostType, coreCostValue);

        List<CostTypes> dependentCosts = costTypesService.getCostTypesByProductTypeSortedByPriority(productTypeId);
        dependentCosts.forEach(costType -> {
            Long value = evaluateFormula(costType.getFormula(), context);
            calculatedCosts.put(costType, value);
            context.put(normalizeName(costType.getName()), value.doubleValue());
        });

        // 4. Lưu tất cả kết quả một lần
        saveAllCostValues(customerChoice, calculatedCosts);
    }

    @Override
    public List<CustomerChoiceCosts> getCustomerChoiceCostByCustomerChoiceId(String customerChoiceId) {
        return customerChoiceCostsRepository.findByCustomerChoices_Id(customerChoiceId);
    }


    // Tính toán giá trị từ công thức SpEL
    private Long evaluateFormula(String formula, Map<String, Object> baseVariables) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(new HashMap<>(baseVariables));
        Expression expression = parser.parseExpression(formula);
        Double result = expression.getValue(context, Double.class);
        return result != null ? Math.round(result) : 0L;
    }

    private Map<String, Object> createBaseContext(CustomerChoices customerChoice) {
        Map<String, Object> variables = new HashMap<>();

        // Thêm các biến từ attributes
        customerChoice.getProductTypes().getAttributes().forEach(attr ->
                variables.put(
                        normalizeName(attr.getName()),
                        0F
                ));

        // Thêm biến từ sizes
        customerChoice.getCustomerChoiceSizes().forEach(size ->
                variables.put(
                        normalizeName(size.getSizes().getName()),
                        size.getSizeValue()
                ));

        // Thêm biến từ attribute details
        customerChoice.getCustomerChoiceDetails().forEach(detail -> {
            String attrName = normalizeName(detail.getAttributeValues().getAttributes().getName());
            Double value = detail.getAttributeValues().getIsMultiplier()
                    ? (detail.getAttributeValues().getUnitPrice() / 10.0)
                    : detail.getAttributeValues().getUnitPrice().doubleValue();
            log.info("attrName: {}, value: {}", attrName, value);
            variables.put(attrName, value);
        });

        return variables;
    }

    private void saveAllCostValues(CustomerChoices customerChoice,
                                   Map<CostTypes, Long> calculatedCosts) {
        // Xóa các cost cũ của customerChoice này
        customerChoiceCostsRepository.deleteByCustomerChoices(customerChoice);

        // Tạo và lưu các cost mới
        List<CustomerChoiceCosts> costsToSave = calculatedCosts.entrySet().stream()
                .map(entry -> {
                    CustomerChoiceCosts cost = new CustomerChoiceCosts();
                    cost.setCustomerChoices(customerChoice);
                    cost.setCostTypes(entry.getKey());
                    cost.setValue(entry.getValue());
                    return cost;
                })
                .collect(Collectors.toList());

        customerChoiceCostsRepository.saveAll(costsToSave);
    }

    private String normalizeName(String name) {
        return name.trim().replaceAll("\\s+", "");
    }
}
