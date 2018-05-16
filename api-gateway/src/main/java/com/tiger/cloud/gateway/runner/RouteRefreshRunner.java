package com.tiger.cloud.gateway.runner;

import com.tiger.cloud.gateway.commons.support.RefreshZuulRouteService;
import com.tiger.cloud.gateway.commons.support.RouteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
public class RouteRefreshRunner implements CommandLineRunner {
    @Autowired
    RefreshZuulRouteService service;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(service);
        RouteManager.setServiec(service);
        RouteManager.init(100);
    }
}
