package com.capstone.ads.utils;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;

@UtilityClass
public class KeyGenerator {
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
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
     * Generates an order code in the format **-**-**-**** (e.g., AB-CD-EF-GHIJ).
     * This method generates the raw code. Uniqueness check should be performed
     * by the calling service (e.g., OrderService) against the database.
     *
     * @return The formatted random alphanumeric order code.
     */
    public static String generateOrderCode() {
        String rawCode = generateRandomAlphanumeric();

        return rawCode.substring(0, 2) + "-" +
                rawCode.substring(2, 4) + "-" +
                rawCode.substring(4, 6) + "-" +
                rawCode.substring(6, 10);
    }
}
