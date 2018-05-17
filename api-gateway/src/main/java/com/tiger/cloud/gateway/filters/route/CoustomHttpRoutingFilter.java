package com.tiger.cloud.gateway.filters.route;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.web.util.UrlPathHelper;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.ROUTE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVICE_ID_KEY;
public abstract class CoustomHttpRoutingFilter extends ZuulFilter {
    protected ProxyRequestHelper helper;
    protected RouteLocator routeLocator;
    protected ZuulProperties properties;
    protected UrlPathHelper urlPathHelper = new UrlPathHelper();
    public CoustomHttpRoutingFilter(ProxyRequestHelper helper, RouteLocator routeLocator, ZuulProperties properties) {
        this.helper = helper;
        this.routeLocator = routeLocator;
        this.properties = properties;
    }

    @Override
    public String filterType() {
        return ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return (ctx.getRouteHost() == null && ctx.get(SERVICE_ID_KEY) != null
                && ctx.sendZuulResponse());
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        //从locator获取服务地址
        final String requestURI = urlPathHelper.getPathWithinApplication(ctx.getRequest());
        Route route = routeLocator.getMatchingRoute(requestURI);
        if(null == route){

        }
        return null;
    }
}
