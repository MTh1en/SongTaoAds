package com.capstone.ads.service.impl;

import com.capstone.ads.dto.customer_choice.CustomerChoicesDTO;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.CustomerChoicesMapper;
import com.capstone.ads.model.*;
import com.capstone.ads.repository.internal.CustomerChoicesRepository;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerChoicesServiceImpl implements CustomerChoicesService {
    UserService userService;
    ProductTypesService productTypesService;
    CustomerChoiceCostsService customerChoiceCostsService;
    CustomerChoicesRepository customerChoicesRepository;
    CustomerChoicesMapper customerChoicesMapper;
    ExpressionParser parser = new SpelExpressionParser();

    @Override
    @Transactional
    public CustomerChoicesDTO createCustomerChoice(String customerId, String productTypeId) {
        Users users = userService.getUserByIdAndIsActive(customerId);
        ProductTypes productTypes = productTypesService.getProductTypeByIdAndAvailable(productTypeId);

        CustomerChoices customerChoices = CustomerChoices.builder()
                .users(users)
                .productTypes(productTypes)
                .totalAmount(0L)
                .build();
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
        ProductTypes productTypes = customerChoices.getProductTypes();
        String totalFormula = productTypes.getCalculateFormula().trim();
        var customerChoiceCostList = customerChoiceCostsService.getCustomerChoiceCostByCustomerChoiceId(customerChoices.getId());

        Map<String, Object> variables = createBaseContext(customerChoiceCostList);
        Long result = evaluateFormula(totalFormula, variables);

        customerChoices.setTotalAmount(result);
        customerChoicesRepository.save(customerChoices);
    }

    private Long evaluateFormula(String formula, Map<String, Object> baseVariables) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(new HashMap<>(baseVariables));
        Expression expression = parser.parseExpression(formula);
        Double result = expression.getValue(context, Double.class);
        return result != null ? Math.round(result) : 0L;
    }

    private Map<String, Object> createBaseContext(List<CustomerChoiceCosts> customerChoiceCosts) {
        Map<String, Object> variables = new HashMap<>();

        customerChoiceCosts.forEach(cost -> {
            String costTypeName = normalizeName(cost.getCostTypes().getName());
            Long costValue = cost.getValue() == null ? 0L : cost.getValue();
            variables.put(costTypeName, costValue);
        });

        return variables;
    }

    private String normalizeName(String name) {
        return name.trim().replaceAll("\\s+", "");
    }
}
