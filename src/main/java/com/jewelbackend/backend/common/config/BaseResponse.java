package com.jewelbackend.backend.common.config;

import java.io.Serializable;


public abstract class BaseResponse implements Serializable {

    private static final long serialVersionUID = 7647491160624932995L;
    private String code;
    private String status;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

