package com.capstone.ads.utils;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SpelFormulaEvaluator {
    private final Map<String, Expression> formulaCache = new ConcurrentHashMap<>();
    private final ExpressionParser parser = new SpelExpressionParser();

    public Long evaluateFormula(String formula, Map<String, Object> variables) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(new HashMap<>(variables));
        Expression expression = formulaCache.computeIfAbsent(formula, parser::parseExpression);
        Double result = expression.getValue(context, Double.class);
        return result != null ? Math.round(result) : 0L;
    }
}
