package com.tiger.cloud.consumer.controller;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/user")
public class UserInfoController {
    @Autowired
    private EurekaClient eurekaClient;
    @Autowired
    private RestTemplate httpclient;
    @RequestMapping("/info")
    @ResponseBody
    public Object userInfo(){
        InstanceInfo instance = eurekaClient.getNextServerFromEureka("user-service",false);
        if(null == instance){ return null;}
        String host = instance.getHostName();
        int port = instance.getPort();
        String url = String.format("http://%s:%d/",host,port);
       ResponseEntity<String> response =  httpclient.getForEntity(url,String.class);
        return response;
    }
}
