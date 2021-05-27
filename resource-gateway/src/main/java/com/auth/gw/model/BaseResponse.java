package com.auth.gw.model;

import lombok.Data;

@Data
public class BaseResponse<T> {
    private long code;
    private String message;
    private T data;

    protected BaseResponse() {
    }

    protected BaseResponse(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<T>(GatewayCode.SUCCESS.getCode(), GatewayCode.SUCCESS.getMessage(), data);
    }

    public static <T> BaseResponse<T> success(T data, String message) {
        return new BaseResponse<T>(GatewayCode.SUCCESS.getCode(), message, data);
    }

    public static <T> BaseResponse<T> failed(String message) {
        return new BaseResponse<T>(GatewayCode.FAILED.getCode(), message, null);
    }

    public static <T> BaseResponse<T> validateFailed(String message) {
        return new BaseResponse<T>(GatewayCode.VALIDATE_FAILED.getCode(), message, null);
    }

    public static <T> BaseResponse<T> unauthorized(T data) {
        return new BaseResponse<T>(GatewayCode.UNAUTHORIZED.getCode(), GatewayCode.UNAUTHORIZED.getMessage(), data);
    }

    public static <T> BaseResponse<T> forbidden(T data) {
        return new BaseResponse<T>(GatewayCode.FORBIDDEN.getCode(), GatewayCode.FORBIDDEN.getMessage(), data);
    }
}