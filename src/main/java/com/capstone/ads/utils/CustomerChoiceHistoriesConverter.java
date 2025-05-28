package com.capstone.ads.utils;

import com.capstone.ads.mapper.CustomerChoiceHistoriesMapper;
import com.capstone.ads.model.CustomerChoices;
import com.capstone.ads.model.json.CustomerChoiceHistories;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerChoiceHistoriesConverter {
    private final CustomerChoiceHistoriesMapper mapper; // MapStruct mapper

    public CustomerChoiceHistories convertToHistory(CustomerChoices customerChoices) {
        CustomerChoiceHistories history = mapper.toProductTypeInCustomerChoiceHistories(customerChoices);

        history.setAttributeSelections(customerChoices.getCustomerChoiceDetails().stream()
                .map(mapper::toAttributeSelection)
                .collect(Collectors.toList()));

        history.setSizeSelections(
                customerChoices.getCustomerChoiceSizes().stream()
                        .map(mapper::toSizeSelection)
                        .collect(Collectors.toList())
        );

        return history;
    }
}
