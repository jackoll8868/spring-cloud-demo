package com.tiger.cloud.gateway.filters.pre;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.tiger.cloud.gateway.Response.ZuulResponse;
import com.tiger.cloud.gateway.enums.ErrorEnum;
import com.tiger.cloud.gateway.enums.ZuulFilterTypes;
import com.tiger.cloud.gateway.exception.BizException;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.cert.ocsp.Req;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthFilter extends ZuulFilter {
    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);
    @Override
    public String filterType() {
        return ZuulFilterTypes.PRE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String uri = request.getRequestURI();
        if(uri.contains("/admin")){
            return true;
        }
        return false;
    }

    @Override
    public Object run() {
        logger.info("Auth filter invoking....");
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(200);
            throw new BizException(ErrorEnum.NOT_LOGIN);
//            context.setResponseBody(JSONObject.toJSONString(new ZuulResponse("000000","You do not login.")));
        }
        return null;
    }
}
