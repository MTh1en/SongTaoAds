package com.capstone.ads.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@Slf4j
public class DataConverter {
    @Value("${stable-diffusion.pixel.max}")
    private long maxPixelValue;

    @Value("${stable-diffusion.pixel.min}")
    private long minPixelValue;

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
}
