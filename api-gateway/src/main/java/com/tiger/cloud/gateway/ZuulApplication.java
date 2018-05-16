package com.tiger.cloud.gateway;

import com.netflix.zuul.FilterLoader;
import com.netflix.zuul.groovy.GroovyCompiler;
import com.tiger.cloud.gateway.commons.support.RefreshZuulRouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.ZuulServerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@EnableAutoConfiguration(exclude = {ZuulServerAutoConfiguration.class})
@EnableZuulProxy
@ComponentScan
@SpringBootApplication
public class ZuulApplication {

    public static void main(String[] args){
        SpringApplication.run(ZuulApplication.class,args);
    }
}
