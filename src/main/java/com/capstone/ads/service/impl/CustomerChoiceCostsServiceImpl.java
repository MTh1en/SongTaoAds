package com.capstone.ads.service.impl;

import com.capstone.ads.dto.customer_choice_cost.CustomerChoiceCostDTO;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.CustomerChoiceCostMapper;
import com.capstone.ads.model.*;
import com.capstone.ads.repository.internal.CustomerChoiceCostsRepository;
import com.capstone.ads.service.CostTypesService;
import com.capstone.ads.service.CustomerChoiceCostsService;
import com.capstone.ads.utils.DataConverter;
import com.capstone.ads.utils.SpelFormulaEvaluator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

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
    SpelFormulaEvaluator formulaEvaluator;

    @Override
    public List<CustomerChoiceCostDTO> findCustomerChoiceCostByCustomerChoice(String customerChoiceId) {
        return getCustomerChoiceCostByCustomerChoiceId(customerChoiceId).stream()
                .map(customerChoiceCostMapper::toDTO)
                .toList();
    }

    //CALCULATE Materiala
    @Override
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
        Long coreCostValue = formulaEvaluator.evaluateFormula(coreCostType.getFormula(), context);
        context.put(DataConverter.normalizeFormulaValueName(coreCostType.getName()), coreCostValue.doubleValue());

        // 5. Tính toán các chi phí khác không phải chi phí core theo mức độ ưu tiên
        Map<CostTypes, Long> calculatedCosts = new HashMap<>();
        calculatedCosts.put(coreCostType, coreCostValue);

        dependentCosts.forEach(costType -> {
            Long value = 0L;
            if (costType.getIsAvailable().equals(true)) {
                value = formulaEvaluator.evaluateFormula(costType.getFormula(), context);
            }
            calculatedCosts.put(costType, value);
            context.put(DataConverter.normalizeFormulaValueName(costType.getName()), value.doubleValue());
        });

        // 6. Lưu tất cả kết quả một lần
        saveAllCostValues(customerChoice, calculatedCosts);
    }

    @Override
    public List<CustomerChoiceCosts> getCustomerChoiceCostByCustomerChoiceId(String customerChoiceId) {
        return customerChoiceCostsRepository.findByCustomerChoices_Id(customerChoiceId);
    }

    private Map<String, Object> createBaseContext(CustomerChoices customerChoice) {
        Map<String, Object> variables = new HashMap<>();

        // Thêm các biến từ attributes
        variables.putAll(customerChoice.getProductTypes().getAttributes().stream()
                .collect(Collectors.toMap(
                        attr -> DataConverter.normalizeFormulaValueName(attr.getName()),
                        attr -> 0F,
                        (v1, v2) -> v1
                )));

        // Thêm biến từ sizes
        variables.putAll(customerChoice.getCustomerChoiceSizes().stream()
                .collect(Collectors.toMap(
                        size -> DataConverter.normalizeFormulaValueName(size.getSizes().getName()),
                        CustomerChoiceSizes::getSizeValue,
                        (v1, v2) -> v1
                )));

        // Thêm biến từ attribute details
        variables.putAll(customerChoice.getCustomerChoiceDetails().stream()
                .collect(Collectors.toMap(
                        detail -> DataConverter.normalizeFormulaValueName(detail.getAttributeValues().getAttributes().getName()),
                        detail -> {
                            if (detail.getAttributeValues().getAttributes().getIsAvailable().equals(true)) {
                                return detail.getAttributeValues().getIsMultiplier()
                                        ? (detail.getAttributeValues().getUnitPrice() / 10.0)
                                        : detail.getAttributeValues().getUnitPrice().doubleValue();
                            } else {
                                return 0.0;
                            }
                        },
                        (v1, v2) -> v1
                )));

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
}
