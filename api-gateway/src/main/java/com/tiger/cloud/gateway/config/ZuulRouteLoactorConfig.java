package com.tiger.cloud.gateway.config;

import com.tiger.cloud.gateway.commons.support.AbstractCustomRouteLocator;
import com.tiger.cloud.gateway.commons.support.CustomerRouteLocaterTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZuulRouteLoactorConfig {
    @Autowired
    private ZuulProperties zuulProperties;
    @Autowired
    private ServerProperties server;

    @Bean
    public AbstractCustomRouteLocator routeLocator(){
        CustomerRouteLocaterTemplate ruouteLocator = new CustomerRouteLocaterTemplate(server.getServletPath(),zuulProperties);
        return ruouteLocator;
    }

    public ZuulProperties getZuulProperties() {
        return zuulProperties;
    }

    public void setZuulProperties(ZuulProperties zuulProperties) {
        this.zuulProperties = zuulProperties;
    }

    public ServerProperties getServer() {
        return server;
    }

    public void setServer(ServerProperties server) {
        this.server = server;
    }

}
