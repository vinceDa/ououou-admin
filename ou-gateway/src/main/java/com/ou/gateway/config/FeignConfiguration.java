package com.ou.gateway.config;

import com.ou.gateway.util.RedisUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

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
        requestTemplate.header("secret", redisUtil.get("secret").toString());
    }
}
