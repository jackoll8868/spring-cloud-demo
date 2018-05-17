package com.tiger.cloud.gateway.config;

import com.netflix.zuul.FilterFileManager;
import com.netflix.zuul.FilterLoader;
import com.netflix.zuul.groovy.GroovyCompiler;
import com.netflix.zuul.groovy.GroovyFileFilter;
import com.tiger.cloud.gateway.filters.support.CustomZuulServletFilter;
import com.tiger.cloud.gateway.filters.error.ZuulGloableErrorFilter;
import com.tiger.cloud.gateway.filters.post.ResponseConverter;
import org.springframework.cloud.netflix.zuul.filters.post.SendResponseFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZuulFilterConfig {
    @Bean
    public CustomZuulServletFilter customZuulServletFilter(){
        return new CustomZuulServletFilter();
    }

    @Bean
    public ZuulGloableErrorFilter errorFilter(){
        return new ZuulGloableErrorFilter();
    }

    @Bean
    public ResponseConverter responseConverter(){
        return  new ResponseConverter();
    }
    @Bean
    public SendResponseFilter sendResponseFilter(){
        return new SendResponseFilter();
    }

//    @Bean
    public FilterFileManager groovy() throws Exception {
        FilterLoader.getInstance().setCompiler(new GroovyCompiler());
        FilterFileManager.setFilenameFilter(new GroovyFileFilter());
        FilterFileManager.init(10,new String[]{
                "api-gateway/src/main/resources/groovy/script/pre",
                "api-gateway/src/main/resources/groovy/script/post",
                "api-gateway/src/main/resources/groovy/script/route",
                "api-gateway/src/main/resources/groovy/script/error"
        });
        return FilterFileManager.getInstance();
    }
}
