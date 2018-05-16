package com.tiger.cloud.gateway.commons.support;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.*;

public class CustomRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {
    private static final Logger logger = LoggerFactory.getLogger(CustomRouteLocator.class);
//    private StringRedisTemplate jdbcTemplate;
    private ZuulProperties properties;

//    public StringRedisTemplate getJdbcTemplate() {
//        return jdbcTemplate;
//    }
//
//    public void setJdbcTemplate(StringRedisTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }

    public ZuulProperties getProperties() {
        return properties;
    }

    public void setProperties(ZuulProperties properties) {
        this.properties = properties;
    }

    public CustomRouteLocator(String servletPath, ZuulProperties properties) {
        super(servletPath, properties);
        this.properties = properties;
        logger.info("[init CustomRouteLocator] servletPath:{}",servletPath);
    }

    @Override
    protected Map<String,ZuulRoute> locateRoutes() {
        HashMap<String,ZuulRoute> routesMap = new LinkedHashMap<String,ZuulRoute>();
        //从properties获取配置
        routesMap.putAll(super.locateRoutes());
        //从db加载路由信息
        routesMap.putAll(locateRoutesFromDB());
        LinkedHashMap<String,ZuulRoute> buffers = new LinkedHashMap<String,ZuulRoute>();
        for(Map.Entry<String,ZuulRoute> entry : routesMap.entrySet()){
            String path = entry.getKey();
            if(!path.startsWith("/")){
                path = "/" + path;
            }
            if(!StringUtils.isEmpty(properties.getPrefix())){
                path = properties.getServletPath() + path;
                if(!path.startsWith("/")){
                    path = "/" + path;
                }

                buffers.put(path,entry.getValue());
            }
            logger.info("route info:[{}-->{}]",path,entry.getValue());
        }
        return buffers;
    }

    private Map<? extends String,? extends ZuulRoute> locateRoutesFromDB() {
        Map<String, ZuulRoute> routes = new LinkedHashMap<>();
//        List<ZuulRouteVO> results = jdbcTemplate.query("select * from gateway_api_define where enabled = true ",new BeanPropertyRowMapper<>(ZuulRouteVO.class));
//        for (ZuulRouteVO result : results) {
//            if(StringUtils.isBlank(result.getPath()) || StringUtils.isBlank(result.getUrl()) ){
//                continue;
//            }
//            ZuulRoute zuulRoute = new ZuulRoute();
//            try {
//                org.springframework.beans.BeanUtils.copyProperties(result,zuulRoute);
//            } catch (Exception e) {
//                logger.error("=============load zuul route info from db with error==============",e);
//            }
//            routes.put(zuulRoute.getPath(),zuulRoute);
//        }
        return routes;
    }

    @Override
    public void refresh() {
        doRefresh();
    }

    static class ZuulRouteVO{
        /**
         * The ID of the route (the same as its map key by default).
         */
        private String id;

        /**
         * The path (pattern) for the route, e.g. /foo/**.
         */
        private String path;

        /**
         * The service ID (if any) to map to this route. You can specify a physical URL or
         * a service, but not both.
         */
        private String serviceId;

        /**
         * A full physical URL to map to the route. An alternative is to use a service ID
         * and service discovery to find the physical address.
         */
        private String url;

        /**
         * Flag to determine whether the prefix for this route (the path, minus pattern
         * patcher) should be stripped before forwarding.
         */
        private boolean stripPrefix = true;

        /**
         * Flag to indicate that this route should be retryable (if supported). Generally
         * retry requires a service ID and ribbon.
         */
        private Boolean retryable;

        private Boolean enabled;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isStripPrefix() {
            return stripPrefix;
        }

        public void setStripPrefix(boolean stripPrefix) {
            this.stripPrefix = stripPrefix;
        }

        public Boolean getRetryable() {
            return retryable;
        }

        public void setRetryable(Boolean retryable) {
            this.retryable = retryable;
        }

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }
    }
}
