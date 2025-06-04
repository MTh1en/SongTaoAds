package com.capstone.ads.utils;

import lombok.experimental.UtilityClass;

import java.util.Base64;

@UtilityClass
public class DataConverter {
    public static String convertByteArrayToBase64(byte[] byteArray) {
        return Base64.getEncoder().encodeToString(byteArray);
    }

    public static byte[] convertBase64ToByteArray(String base64) {
        return Base64.getDecoder().decode(base64);
    }
}
