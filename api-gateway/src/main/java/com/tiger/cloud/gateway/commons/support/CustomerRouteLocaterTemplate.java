package com.tiger.cloud.gateway.commons.support;

import com.google.common.collect.Maps;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

import java.util.HashMap;
import java.util.Map;

public class CustomerRouteLocaterTemplate extends AbstractCustomRouteLocator {

    public CustomerRouteLocaterTemplate(String servletPath, ZuulProperties properties) {
        super(servletPath, properties);
    }

    @Override
    protected Map<? extends String, ? extends ZuulProperties.ZuulRoute> customerRoute(HashMap<String, ZuulProperties.ZuulRoute> routesMap) {
        return routesMap;
    }

    @Override
    protected Map<? extends String, ? extends ZuulProperties.ZuulRoute> locateRoutesFromDatabase() {
        return Maps.newHashMap();
    }

    @Override
    protected Map<? extends String, ? extends ZuulProperties.ZuulRoute> locateRouesFromRedis() {
        return Maps.newHashMap();
    }

    @Override
    protected Map<? extends String, ? extends ZuulProperties.ZuulRoute> locateRouesFromZookeeper() {
        return Maps.newHashMap();
    }

    @Override
    protected Map<? extends String, ? extends ZuulProperties.ZuulRoute> locateRouesFromFile() {
        return Maps.newHashMap();
    }
}
