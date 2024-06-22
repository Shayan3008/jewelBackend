package com.jewelbackend.backend.common.config;


import org.springframework.http.HttpHeaders;

public class ResponseReport<E> extends BaseResponse {

    private static final long serialVersionUID = -516999376496395557L;
    private E data;
    private HttpHeaders httpHeaders;

    public ResponseReport(String status, String message, String code, E data, HttpHeaders httpHeaders) {
        super();
        super.setStatus(status);
        super.setMessage(message);
        super.setCode(code);
        this.data = data;
        this.setHttpHeaders(httpHeaders);
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
    }
}

