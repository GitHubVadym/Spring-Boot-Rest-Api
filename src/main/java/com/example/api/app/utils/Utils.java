package com.example.api.app.utils;

import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Component;

@Component
public class Utils {
    private Utils() {
    }

    public String generateRandomString(int len) {
        RandomString randomString = new RandomString(len);
        return randomString.nextString();
    }
}
