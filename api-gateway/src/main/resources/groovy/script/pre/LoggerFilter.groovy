package groovy.script.pre

import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.util.StreamUtils

import javax.servlet.http.HttpServletRequest
import java.nio.charset.Charset

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.*;

class LoggerFilter extends ZuulFilter {
    static final Logger log = LoggerFactory.getLogger(LoggerFilter.class);

    @Override
    String filterType() {
        return PRE_TYPE
    }

    @Override
    int filterOrder() {
        return -10
    }

    @Override
    boolean shouldFilter() {
        return true
    }

    @Override
    Object run() {
        log.info("[zuul filter] request begin......");
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest httpRequest = ctx.getRequest();
        String method = httpRequest.getMethod();
        Enumeration<String> header = httpRequest.getHeaderNames();
        String headStr = convertHeader(header, httpRequest);
        String paraStr = convertParameters(httpRequest);
        String scheme = httpRequest.getScheme();
        //OTHER Data
        String data = getPOSTData(httpRequest);
        StringBuffer sb = new StringBuffer();
        sb.append("\n").append(scheme).append("\t").append(method).append("\t").append(httpRequest.getRequestURI())
                .append("?").append(paraStr).append("\n");
        sb.append(headStr).append("\n");
        sb.append("\t\n");
        sb.append(data);
        log.info("[zuul filter] http raw request:{}", sb.toString());
        return null
    }

    String getPOSTData(HttpServletRequest request) {
        String buffer = StreamUtils.copyToString(request.getInputStream(), Charset.forName("UTF-8"));
        return buffer;
    }

    String convertHeader(final Enumeration<String> stringEnumeration, final HttpServletRequest request) {
        final StringBuffer sb = new StringBuffer();
        sb.append("\n");
        stringEnumeration.collect {
            x -> sb.append("\n").append(x).append(":").append(request.getHeader(x))
        }

        return sb.deleteCharAt(0).toString()
    }

    String convertParameters(final HttpServletRequest request) {
        final StringBuffer sb = new StringBuffer();
        Enumeration<String> keys = request.getParameterNames();
        keys.each {sb.append("&").append(it).append("=").append(request.getParameter(it))}
        return sb.deleteCharAt(sb.length() > 1 ? 1: 0).toString()
    }
}
