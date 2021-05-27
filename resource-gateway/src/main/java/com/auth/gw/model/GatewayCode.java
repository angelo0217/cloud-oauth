package com.auth.gw.model;

public enum GatewayCode {
    SUCCESS(200, "success"),
    FAILED(500, "error"),
    VALIDATE_FAILED(404, "request filed error"),
    UNAUTHORIZED(401, "token error"),
    FORBIDDEN(403, "not auth");
    private long code;
    private String message;

    private GatewayCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}