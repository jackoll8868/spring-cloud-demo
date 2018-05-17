package com.tiger.cloud.gateway.commons.support;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;

import java.util.*;

/**
 * 用来确定具体的服务对应关系
 */
public abstract class AbstractCustomRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {
    private static final Logger logger = LoggerFactory.getLogger(AbstractCustomRouteLocator.class);
    protected ZuulProperties properties;

    public ZuulProperties getProperties() {
        return properties;
    }

    public void setProperties(ZuulProperties properties) {
        this.properties = properties;
    }

    public AbstractCustomRouteLocator(String servletPath, ZuulProperties properties) {
        super(servletPath, properties);
        this.properties = properties;
        logger.info("[init AbstractCustomRouteLocator] servletPath:{}",servletPath);
    }

    @Override
    protected Map<String,ZuulRoute> locateRoutes() {
        HashMap<String,ZuulRoute> routesMap = new LinkedHashMap<String,ZuulRoute>();
        //从properties获取配置
        routesMap.putAll(super.locateRoutes());
        //从db加载路由信息
        routesMap.putAll(locateRoutesFromDatabase());
        routesMap.putAll(locateRouesFromRedis());
        routesMap.putAll(locateRouesFromZookeeper());
        routesMap.putAll(locateRouesFromFile());
        LinkedHashMap<String,ZuulRoute> buffers = new LinkedHashMap<String,ZuulRoute>();
        customerRoute(routesMap);
        return buffers;
    }

    protected abstract Map<? extends String,? extends ZuulProperties.ZuulRoute> customerRoute(HashMap<String,ZuulRoute> routesMap);

    protected abstract Map<? extends String,? extends ZuulRoute> locateRoutesFromDatabase() ;
    protected abstract Map<? extends String,? extends ZuulRoute> locateRouesFromRedis() ;
    protected abstract Map<? extends String,? extends ZuulRoute> locateRouesFromZookeeper() ;
    protected abstract Map<? extends String,? extends ZuulRoute> locateRouesFromFile() ;

    @Override
    public void refresh() {
        doRefresh();
    }


}
