package com.tiger.cloud.gateway.filters.error;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.tiger.cloud.gateway.Response.ZuulResponse;
import com.tiger.cloud.gateway.enums.ZuulFilterTypes;
import com.tiger.cloud.gateway.exception.BizException;
import org.bouncycastle.cert.ocsp.Req;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.ERROR_TYPE;

public class ZuulGloableErrorFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return ERROR_TYPE;
    }

    @Override
    public int filterOrder() {
        return -1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        return context.getThrowable() != null ? true : false;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
//        context.setSendZuulResponse(false);//过滤改请求不进行路由
        Throwable ex = context.getThrowable();
        if(null == ex) return null;
        if(ex instanceof BizException){
            BizException ex$ = (BizException)ex;
            String code = ex$.getCode();
            String msg = ex$.getMsg();
//            return new ZuulResponse(code,msg);
            context.setResponseBody(JSONObject.toJSONString( new ZuulResponse(code,msg)));
        }
        else{
            context.setResponseStatusCode(200);
            context.setResponseBody(JSONObject.toJSONString(ZuulResponse.fail()));
        }
        return null;
    }
}
