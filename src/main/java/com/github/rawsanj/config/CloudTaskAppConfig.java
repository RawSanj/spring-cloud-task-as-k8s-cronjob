package com.github.rawsanj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CloudTaskAppConfig {

    @Bean
    public TaskListener taskListener(){
        return new TaskListener();
    }

    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
