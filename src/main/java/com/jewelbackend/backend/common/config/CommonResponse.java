package com.jewelbackend.backend.common.config;

import lombok.Data;

@Data
public class CommonResponse<T> {

    private String message;
    private int statusCode;
    private T body;
    private long size = 1;

    public CommonResponse(String message, int statusCode, T body, long size) {
        this.message = message;
        this.statusCode = statusCode;
        this.body = body;
        this.size = size;
    }

    public CommonResponse(String message, int statusCode, T body) {
        this.message = message;
        this.statusCode = statusCode;
        this.body = body;
    }

}
