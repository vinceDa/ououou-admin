package com.ou.gateway.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author vince
 * @date 2019/12/16 16:14
 */
@Data
@ConfigurationProperties(prefix = "auth")
@Component
public class PermitUrlProperties {

    private List<String> permitPatterns;


    public boolean isPermitPatterns(String url) {
        return permitPatterns.contains(url);
    }

    public List<String> getPermitPatterns() {
        return permitPatterns;
    }

    /*    @Bean
    public PermitAllUrlProperties getPermitAllUrlProperties() {
        return new PermitAllUrlProperties();
    }*/
}
