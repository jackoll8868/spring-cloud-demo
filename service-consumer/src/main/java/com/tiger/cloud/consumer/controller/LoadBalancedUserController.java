package com.tiger.cloud.consumer.controller;

import com.tiger.cloud.consumer.config.MyFeignConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(name="user-service",configuration = MyFeignConfiguration.class)
@Controller
@RequestMapping("/userx")
public interface LoadBalancedUserController {
    @RequestMapping("info")
    @ResponseBody
    Object info();

}
