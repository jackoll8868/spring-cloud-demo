package com.tiger.cloud.gateway.enums;

public enum  ErrorEnum {
    NOT_LOGIN("90001","用户未登入");

    private final String code;
    private final String msg;

    ErrorEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
