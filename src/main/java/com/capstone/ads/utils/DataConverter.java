package com.capstone.ads.utils;

import com.capstone.ads.mapper.CustomerChoiceHistoriesMapper;
import com.capstone.ads.model.CustomerChoices;
import com.capstone.ads.model.json.CustomerChoiceHistories;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataConverter {
    @Value("${stable-diffusion.pixel.max}")
    private long maxPixelValue;

    @Value("${stable-diffusion.pixel.min}")
    private long minPixelValue;

    private final CustomerChoiceHistoriesMapper mapper;

    public static String convertByteArrayToBase64(byte[] byteArray) {
        return Base64.getEncoder().encodeToString(byteArray);
    }

    public static byte[] convertBase64ToByteArray(String base64) {
        return Base64.getDecoder().decode(base64);
    }

    public static int convertDoubleToInt(double d) {
        long rounded = Math.round(d);
        return Math.toIntExact(rounded);
    }

    public static int convertFloatToInt(float f) {
        return Math.round(f);
    }

    public long convertSizeValueToPixelValue(Float sizeValue,
                                             Float minSizeValue, Float maxSizeValue) {
        float pixelValue = minPixelValue + (maxPixelValue - minPixelValue) *
                (sizeValue - minSizeValue) / (maxSizeValue - minSizeValue);
        return convertFloatToInt(pixelValue);
    }

    public static String normalizeFormulaValueName(String name) {
        if (name == null) {
            return "";
        }

        StringBuilder normalized = new StringBuilder();
        for (char c : name.trim().toCharArray()) {
            if (!Character.isWhitespace(c)) {
                normalized.append(c);
            }
        }

        return normalized.toString();
    }

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
