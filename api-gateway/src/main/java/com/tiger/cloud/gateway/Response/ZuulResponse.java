package com.tiger.cloud.gateway.Response;

public class ZuulResponse<T> {
    private String code;
    private String message;
    private T data;

    public ZuulResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public ZuulResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
    public static ZuulResponse ok(){
        return new ZuulResponse("000000","success",null);
    }

    public  ZuulResponse ok(T data){
        return new ZuulResponse("000000","success",data);
    }

    public  static ZuulResponse fail(){
        return new ZuulResponse("999999","failed",null);
    }

    public ZuulResponse fail(T data){
        return new ZuulResponse("999999","failed",data);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

