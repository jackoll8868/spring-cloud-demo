package com.tiger.cloud.eurekaclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@EnableAutoConfiguration
@Configuration
@ComponentScan
@Controller
@SpringBootApplication
public class EurekaClientDemo {
    @RequestMapping("/")
    public String home() {
        return "Hello~ This is the user service";
    }

    @RequestMapping("/userx/info")
    @ResponseBody
    public User info(){

        User user = new User("Allen","20","SHENZHEN. CHINA","allen@gmail.com","+8618888888888");
        return user;
    }


    public static void main(String[] args) {
        SpringApplication.run(EurekaClientDemo.class, args);
    }
}