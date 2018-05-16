package com.tiger.cloud.gateway.filters.post;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.tiger.cloud.gateway.Response.ZuulResponse;
import com.tiger.cloud.gateway.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.*;

public class ResponseConverter extends ZuulFilter {
    private static final Logger logger = LoggerFactory.getLogger(ResponseConverter.class);
    @Override
    public String filterType() {
        return POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 500;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletResponse rawResponse = context.getResponse();
        rawResponse.addHeader("zuul-uuid", UUID.randomUUID().toString());
        rawResponse.addHeader("Content-Type", "application/json");
        if (null == context.getThrowable()) {//正常情况
            if (context.get("zuulResponse") != null) {
                Map<String, Object> response = new HashMap<String, Object>();
                response.put("code", "000000");
                response.put("msg", "success");
                ClientHttpResponse httpResponse = (ClientHttpResponse) context.get("zuulResponse");
                BufferedReader reader = null;//防止乱码
                try {
                    reader = new BufferedReader(new InputStreamReader(httpResponse.getBody(),"utf-8"));

                String buffer  = "";
                StringBuffer sb = new StringBuffer();
                while((buffer = reader.readLine())!=null){
                    sb.append(buffer);
                }
                response.put("data", JSONObject.parse(sb.toString()));
                logger.info("[zuul-post-responseconverter] response:{}",JSONObject.toJSONString(response));

                context.setResponseBody(JSONObject.toJSONString(response));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Throwable ex = context.getThrowable();
            if (null == ex) return null;
            if (ex instanceof BizException) {
                BizException ex$ = (BizException) ex;
                String code = ex$.getCode();
                String msg = ex$.getMsg();
                Map<String, Object> response = new HashMap<String, Object>();
                response.put("code", "999999");
                response.put("msg", "failed");
                response.put("data",new ZuulResponse(code, msg));
                logger.info("[zuul-post-responseconverter] response:{}",JSONObject.toJSONString(response));
                context.setResponseBody(JSONObject.toJSONString(response));
            } else {
                Map<String, Object> response = new HashMap<String, Object>();
                response.put("code", "99999");
                response.put("msg", "failed");
                response.put("data",ZuulResponse.fail());
                context.setResponseBody(JSONObject.toJSONString(response));
                logger.info("[zuul-post-responseconverter] response:{}",JSONObject.toJSONString(response));

            }
            //清理掉throwable参数避免后续的SendResponseFilter不被执行
           context.remove("throwable");
        }

        return null;
    }
}
