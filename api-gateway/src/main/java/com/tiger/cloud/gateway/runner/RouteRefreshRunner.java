package com.tiger.cloud.gateway.runner;

import com.netflix.zuul.monitoring.MonitoringHelper;
import com.tiger.cloud.gateway.commons.support.AbstractCustomRouteLocator;
import com.tiger.cloud.gateway.commons.support.RouteManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RouteRefreshRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(RouteRefreshRunner.class);
    @Value("${zuul.route.refresh.enabled}")
    private boolean refreshEnabled;
    @Value("${zuul.route.fresh-time-offset}")
    private int refreshTimeout;
    @Autowired
    private AbstractCustomRouteLocator locator;

    @Override
    public void run(String... args) throws Exception {
        MonitoringHelper.initMocks();
        try{
            if(refreshEnabled){
                RouteManager.setRouteLocator(locator);
                RouteManager.init(refreshTimeout);
                log.info("start custome Route info runner...");
            }

        }catch(Exception ex){
            log.error("RouteRefreshRunner error:{}",ex);
            throw new RuntimeException(ex);
        }



    }
}
