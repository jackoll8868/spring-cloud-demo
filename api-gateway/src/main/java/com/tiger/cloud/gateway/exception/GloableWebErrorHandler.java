package com.tiger.cloud.gateway.exception;

import com.alibaba.fastjson.JSONObject;
import com.tiger.cloud.gateway.Response.ZuulResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@ResponseBody
public class GloableWebErrorHandler {
    @ExceptionHandler(value=Throwable.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Object errorHandler(HttpServletRequest request,Exception ex) throws Throwable{
        if(null == ex) return ZuulResponse.fail();
        if(ex instanceof BizException){
            BizException ex$ = (BizException)ex;
            String code = ex$.getCode();
            String msg = ex$.getMsg();
            return new ZuulResponse(code,msg);
        }
        return ZuulResponse.fail();
    }
}
