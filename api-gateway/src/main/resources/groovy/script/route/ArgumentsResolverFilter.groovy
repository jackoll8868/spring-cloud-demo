package groovy.script.route;

import com.netflix.zuul.ZuulFilter
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.*;

/**
 * 对已经解密/验签的数据进行转换，如果需要的话
 */
class ArgumentsResolverFilter extends ZuulFilter{
    @Override
    String filterType() {
        return ROUTE_TYPE
    }

    @Override
    int filterOrder() {
        return 0
    }

    @Override
    boolean shouldFilter() {
        return false
    }

    @Override
    Object run() {
        return null
    }
}
