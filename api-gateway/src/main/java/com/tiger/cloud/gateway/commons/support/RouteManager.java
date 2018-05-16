package com.tiger.cloud.gateway.commons.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class RouteManager {
    private static Logger logger = LoggerFactory.getLogger(RouteManager.class);
    RefreshZuulRouteService service;
    int pollingIntervalSeconds;
    Thread poller;
    boolean bRunning = true;

    static RouteManager INSTANCE;

    private RouteManager() {
    }

    public static RouteManager getInstance() {
        if (INSTANCE == null) INSTANCE = new RouteManager();
        return INSTANCE;
    }

    public static void setServiec(RefreshZuulRouteService service) {
        if (INSTANCE == null) INSTANCE = new RouteManager();
        INSTANCE.service = service;
    }

    public static void init(int offset) {
        if (INSTANCE == null) INSTANCE = new RouteManager();
        INSTANCE.pollingIntervalSeconds = offset;
        INSTANCE.startPoller();
    }

    void stopPoller() {
        bRunning = false;
    }

    void startPoller() {
        poller = new Thread("Dynamic route reader") {
            public void run() {
                while (bRunning) {
                    try {
                        sleep(pollingIntervalSeconds * 1000);
                        refreshRoutes();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        poller.setDaemon(true);
        poller.start();
    }

    private void refreshRoutes() {
        service.refreshRoute();
    }
}
