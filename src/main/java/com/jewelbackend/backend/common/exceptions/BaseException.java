package com.jewelbackend.backend.common.exceptions;

public class BaseException extends Exception {
    private final String message;

    public BaseException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
