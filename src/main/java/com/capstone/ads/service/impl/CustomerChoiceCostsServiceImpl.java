package com.capstone.ads.service.impl;

import com.capstone.ads.dto.customer_choice_cost.CustomerChoiceCostDTO;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.CustomerChoiceCostMapper;
import com.capstone.ads.model.*;
import com.capstone.ads.repository.internal.CustomerChoiceCostsRepository;
import com.capstone.ads.service.CostTypesService;
import com.capstone.ads.service.CustomerChoiceCostsService;
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

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerChoiceCostsServiceImpl implements CustomerChoiceCostsService {
    CostTypesService costTypesService;
    CustomerChoiceCostMapper customerChoiceCostMapper;
    CustomerChoiceCostsRepository customerChoiceCostsRepository;
    ExpressionParser parser = new SpelExpressionParser();

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
        String productTypeId = customerChoice.getProductTypes().getId();

        // 2. Tạo context chứa biến cơ bản (sử dụng dữ liệu đã fetch)
        Map<String, Object> context = createBaseContext(customerChoice);

        // 3. Tải tất cả CostTypes liên quan đến ProductType trong MỘT TRUY VẤN
        // và phân loại chúng trong bộ nhớ.
        List<CostTypes> allCostTypesForProductType = costTypesService.getCostTypesByProductTypeSortedByPriority(productTypeId);

        // Tìm Core Cost Type
        CostTypes coreCostType = allCostTypesForProductType.stream()
                .filter(CostTypes::getIsCore) // Giả sử có getter isCore()
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.COST_TYPE_NOT_FOUND));

        // Lọc các Dependent Costs (loại bỏ core và sắp xếp)
        List<CostTypes> dependentCosts = allCostTypesForProductType.stream()
                .filter(ct -> !ct.getIsCore())
                .sorted(Comparator.comparing(CostTypes::getPriority)) // Đảm bảo đúng thứ tự ưu tiên
                .toList();

        // 4. Tính toán chi phí core
        Long coreCostValue = evaluateFormula(coreCostType.getFormula(), context);
        context.put(normalizeName(coreCostType.getName()), coreCostValue.doubleValue());

        // 5. Tính toán các chi phí khác không phải chi phí core theo mức độ ưu tiên
        Map<CostTypes, Long> calculatedCosts = new HashMap<>();
        calculatedCosts.put(coreCostType, coreCostValue);

        dependentCosts.forEach(costType -> {
            Long value = evaluateFormula(costType.getFormula(), context);
            calculatedCosts.put(costType, value);
            context.put(normalizeName(costType.getName()), value.doubleValue());
        });

        // 6. Lưu tất cả kết quả một lần
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
