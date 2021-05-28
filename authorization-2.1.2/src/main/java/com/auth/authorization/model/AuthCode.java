package com.auth.authorization.model;

public enum AuthCode {
    SUCCESS(200, "success"),
    AUTH_ERROR(403, "auth error"),
    ERROR(999, "system error");
    private long code;
    private String message;

    private AuthCode(long code, String message) {
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