package com.example.api.app.security;

import org.springframework.beans.factory.annotation.Autowired;

import static com.example.api.app.SpringApplicationContext.getBean;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 86400000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users";
    public static final String H2_CONSOLE = "/h2-console/**";
    @Autowired
    AppProperties appProperties;

    public static String getTokenSecret() {
        AppProperties appProperties = (AppProperties) getBean("appProperties");
        return appProperties.getSecurityToken();
    }
}
