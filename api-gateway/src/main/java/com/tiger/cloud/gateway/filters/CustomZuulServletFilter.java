package com.tiger.cloud.gateway.filters;

import com.netflix.zuul.ZuulRunner;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.netflix.zuul.filters.ZuulServletFilter;
import com.tiger.cloud.gateway.commons.utils.SnowflakeIdWorker;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

//继承默认的ZuulServletFilter新增会话级唯一日志 ID
public class CustomZuulServletFilter extends ZuulServletFilter {
    private ZuulRunner zuulRunner;
    private static final SnowflakeIdWorker ID_WORKER = new SnowflakeIdWorker(0, 0);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        String bufferReqsStr = filterConfig.getInitParameter("buffer-requests");
        boolean bufferReqs = bufferReqsStr != null && bufferReqsStr.equals("true") ? true : false;

        zuulRunner = new ZuulRunner(bufferReqs);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        insertLogId();
        try {
            init((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
            try {
                preRouting();
            } catch (ZuulException e) {
                error(e);
                postRouting();
                return;
            }

            // Only forward onto to the chain if a zuul response is not being sent
            if (!RequestContext.getCurrentContext().sendZuulResponse()) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            try {
                routing();
            } catch (ZuulException e) {
                error(e);
                postRouting();
                return;
            }
            try {
                postRouting();
            } catch (ZuulException e) {
                error(e);
                return;
            }
        } catch (Throwable e) {
            error(new ZuulException(e, 500, "UNCAUGHT_EXCEPTION_FROM_FILTER_" + e.getClass().getName()));
        } finally {
            RequestContext.getCurrentContext().unset();
            cleanLogId();
        }
    }

    private void cleanLogId() {
        MDC.clear();
    }

    private void insertLogId() {
        long id = ID_WORKER.nextId();
        MDC.put("PID",id+"");
    }

    void postRouting() throws ZuulException {
        zuulRunner.postRoute();
    }

    void routing() throws ZuulException {
        zuulRunner.route();
    }

    void preRouting() throws ZuulException {
        zuulRunner.preRoute();
    }

    void init(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        zuulRunner.init(servletRequest, servletResponse);
    }

    void error(ZuulException e) {
        RequestContext.getCurrentContext().setThrowable(e);
        zuulRunner.error();
    }

    @Override
    public void destroy() {
    }
}
