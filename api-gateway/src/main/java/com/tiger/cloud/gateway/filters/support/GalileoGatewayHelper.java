package com.tiger.cloud.gateway.filters.support;

import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GalileoGatewayHelper extends ProxyRequestHelper {
    private Logger logger = LoggerFactory.getLogger(GalileoGatewayHelper.class);
    @Override
    public Map<String, Object> debug(String verb, String uri, MultiValueMap<String, String> headers, MultiValueMap<String, String> params, InputStream requestEntity) throws IOException {
        Map<String, Object> info = new LinkedHashMap<>();
        RequestContext context = RequestContext.getCurrentContext();
        info.put("method", verb);
        info.put("path", uri);
        info.put("query", getQueryString(params));
        info.put("remote", true);
        info.put("proxy", context.get("proxy"));
        Map<String, Object> trace = new LinkedHashMap<>();
        Map<String, Object> input = new LinkedHashMap<>();
        trace.put("request", input);
        info.put("headers", trace);
        debugHeaders(headers, input);
        RequestContext ctx = RequestContext.getCurrentContext();
        if (shouldDebugBody(ctx)) {
            // Prevent input stream from being read if it needs to go downstream
            if (requestEntity != null) {
                debugRequestEntity(info, ctx.getRequest().getInputStream());
            }
        }
        return info;
    }

    void debugHeaders(MultiValueMap<String, String> headers, Map<String, Object> map) {
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            Collection<String> collection = entry.getValue();
            Object value = collection;
            if (collection.size() < 2) {
                value = collection.isEmpty() ? "" : collection.iterator().next();
            }
            map.put(entry.getKey(), value);
        }
    }

    @Override
    public void appendDebug(Map<String, Object> info, int status,
                            MultiValueMap<String, String> headers) {
        @SuppressWarnings("unchecked")
        Map<String, Object> trace = (Map<String, Object>) info.get("headers");
        Map<String, Object> output = new LinkedHashMap<String, Object>();
        trace.put("response", output);
        debugHeaders(headers, output);
        output.put("status", "" + status);
    }

    private void debugRequestEntity(Map<String, Object> info, InputStream inputStream)
            throws IOException {
        if (RequestContext.getCurrentContext().isChunkedRequestBody()) {
            info.put("body", "<chunked>");
            return;
        }
        char[] buffer = new char[4096];
        int count = new InputStreamReader(inputStream, Charset.forName("UTF-8"))
                .read(buffer, 0, buffer.length);
        if (count > 0) {
            String entity = new String(buffer).substring(0, count);
            info.put("body", entity.length() < 4096 ? entity : entity + "<truncated>");
        }
    }
}
