package com.example.api.app.exceptions;

public class UserServiceException extends RuntimeException {

    private static final long serialVersionUID = -3907349330811159057L;

    public UserServiceException(String message) {
        super(message);
    }
}
