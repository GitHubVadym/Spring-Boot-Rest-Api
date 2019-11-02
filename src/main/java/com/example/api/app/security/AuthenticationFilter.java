package com.example.api.app.security;

import com.example.api.app.service.userservise.UserService;
import com.example.api.app.ui.model.request.UserLoginRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.example.api.app.SpringApplicationContext.getBean;
import static com.example.api.app.security.SecurityConstants.EXPIRATION_TIME;
import static com.example.api.app.security.SecurityConstants.HEADER_STRING;
import static com.example.api.app.security.SecurityConstants.TOKEN_PREFIX;
import static com.example.api.app.security.SecurityConstants.getTokenSecret;

@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    @Autowired
    UserService userService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            UserLoginRequestModel creds = new ObjectMapper().readValue(request.getInputStream(),
                    UserLoginRequestModel.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) {
        String username = ((User) authResult.getPrincipal()).getUsername();

        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, getTokenSecret())
                .compact();

        UserService userServiceImpl = (UserService) getBean("userServiceImpl");
        String userId = userServiceImpl.getUser(username).getUserId();

        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        response.addHeader("UserId", userId);
    }
}
