package com.tiger.cloud.gateway.exception;


import com.tiger.cloud.gateway.enums.ErrorEnum;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class BizException extends RuntimeException {
    private String code;
    private String msg;

    public BizException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BizException(ErrorEnum error){
        this.code = error.getCode();
        this.msg= error.getMsg();
    }

    public BizException(String message, String code, String msg) {
        super(message);
        this.code = code;
        this.msg = msg;
    }

    public BizException(String message, Throwable cause, String code, String msg) {
        super(message, cause);
        this.code = code;
        this.msg = msg;
    }

    public BizException(Throwable cause, String code, String msg) {
        super(cause);
        this.code = code;
        this.msg = msg;
    }

    public BizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String code, String msg) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
