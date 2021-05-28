package com.auth.authorization.model;

import lombok.Data;

@Data
public class BaseResponse<T> {
    private long code;
    private String message;
    private T data;

    protected BaseResponse() {
    }

    protected BaseResponse(AuthCode authCode) {
        this.code = authCode.getCode();
        this.message = authCode.getMessage();
    }

    protected BaseResponse(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<T>(AuthCode.SUCCESS.getCode(), AuthCode.SUCCESS.getMessage(), data);
    }

    public static <T> BaseResponse<T> success(T data, String message) {
        return new BaseResponse<T>(AuthCode.SUCCESS.getCode(), message, data);
    }

    public static <T> BaseResponse<T> success(AuthCode authCode) {
        return new BaseResponse<T>(authCode);
    }

    public static <T> BaseResponse<T> failed(AuthCode authCode) {
        return new BaseResponse<T>(authCode);
    }
}