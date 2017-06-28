package com.lomoye.common.exception;

/**
 * 系统异常
 * Created by tommy on 2015/6/4.
 */
public class SystemException extends RuntimeException {
    private String code;

    private String message;

    public SystemException(Throwable cause, String code, String message) {
        super(cause);
        this.code = code;
        this.message = message;
    }

    public SystemException(String code) {
        this.code = code;
    }

    public SystemException(Throwable cause) {
        super(cause);
    }

    public SystemException() {
        super();
    }

    public SystemException(String code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

