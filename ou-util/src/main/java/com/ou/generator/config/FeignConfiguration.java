package com.ou.generator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.ou.generator.util.RedisUtil;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * @author vince
 * @date 2019/12/17 20:37
 */
@Configuration
public class FeignConfiguration implements RequestInterceptor {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Access-Control-Allow-Origin", "*");
        requestTemplate.header("secretKey", redisUtil.get("secretKey").toString());
    }
}
