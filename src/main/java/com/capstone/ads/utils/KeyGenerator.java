package com.capstone.ads.utils;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;

@UtilityClass
public class KeyGenerator {
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int ALPHABET_SIZE = ALPHABET.length();
    private static final SecureRandom secureRandom = new SecureRandom(); // An toàn hơn cho ID

    /**
     * Generates a random alphanumeric string of a specified length.
     * Uses SecureRandom for better randomness, suitable for IDs.
     *
     * @return A random string composed of alphanumeric characters (0-9, A-Z).
     */
    private static String generateRandomAlphanumeric() {
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(ALPHABET.charAt(secureRandom.nextInt(ALPHABET_SIZE)));
        }
        return sb.toString();
    }

    /**
     * Generates an order code in the format DH-********** (e.g., DH-AD561VN9A5).
     * This method generates the raw code. Uniqueness check should be performed
     * by the calling service (e.g., OrderService) against the database.
     *
     * @return The formatted random alphanumeric order code.
     */
    public static String generateOrderCode() {
        String rawCode = generateRandomAlphanumeric();

        return String.format("DH-%s", rawCode);
    }

    /**
     * Generates an custom design reqeust code in the format TK-********** (e.g., TK-AD561VN9A5).
     * This method generates the raw code. Uniqueness check should be performed
     * by the calling service (e.g., CustomDesignRequest) against the database.
     *
     * @return The formatted random alphanumeric order code.
     */
    public static String generateCustomDesignRequest() {
        String rawCode = generateRandomAlphanumeric();

        return String.format("TK-%s", rawCode);
    }
}
