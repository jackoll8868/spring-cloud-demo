package com.tiger.cloud.gateway.commons.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class RefreshZuulRouteService {
    private static final Logger log = LoggerFactory.getLogger(RefreshZuulRouteService.class);
    @Autowired
    ApplicationEventPublisher publisher;
    @Autowired
    RouteLocator routeLocator;
    public void refreshRoute(){
        RoutesRefreshedEvent event = new RoutesRefreshedEvent(routeLocator);
        log.info("refresh route rule...");
        publisher.publishEvent(event);
    }
}
