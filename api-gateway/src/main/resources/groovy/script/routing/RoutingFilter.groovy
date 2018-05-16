package groovy.script.route

import com.netflix.zuul.ZuulFilter
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.*;

class RoutingFilter extends ZuulFilter{
    static final Logger log = LoggerFactory.getLogger(RoutingFilter.class);
    @Override
    String filterType() {
        return ROUTE_TYPE
    }

    @Override
    int filterOrder() {
        return -1
    }

    @Override
    boolean shouldFilter() {
        return true
    }

    @Override
    Object run() {
        log.info("Dynamic Router invoked...")
        return null
    }
}
