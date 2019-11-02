package com.example.api.app.utils;

import org.springframework.stereotype.Controller;

import java.security.SecureRandom;
import java.util.Random;

@Controller
public class Utils {
    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String getUserId(int len) {
        return generateRandomString(len);
    }

    public String getAddressId(int len) {
        return generateRandomString(len);
    }

    private String generateRandomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}
