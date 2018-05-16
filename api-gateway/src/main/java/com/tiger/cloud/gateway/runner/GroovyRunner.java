package com.tiger.cloud.gateway.runner;

import com.netflix.zuul.FilterFileManager;
import com.netflix.zuul.FilterLoader;
import com.netflix.zuul.groovy.GroovyCompiler;
import com.netflix.zuul.groovy.GroovyFileFilter;
import com.netflix.zuul.monitoring.MonitoringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class GroovyRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(GroovyRunner.class);
    @Value("${zuul.groovy.path}")
    private  String groovyPath;

    @Override
    public void run(String... args) throws Exception {
        MonitoringHelper.initMocks();
        FilterLoader.getInstance().setCompiler(new GroovyCompiler());
        try {
            FilterFileManager.setFilenameFilter(new GroovyFileFilter());
            FilterFileManager.init(5, new String[]{
                    groovyPath + "pre",
                    groovyPath + "post",
                    groovyPath + "error",
                    groovyPath + "routing"});
            logger.info("Groovy Runner started.The groovy script root path:{}",groovyPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
