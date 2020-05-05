/*
package com.ou.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

*/
/**
 *  Swagger2配置类
 * @author vince
 * @date 2019/10/15 15:00
 *//*

@Configuration
public class Swagger2 {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ou"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        String author = "ououou";
        String url = "https://github.com/vinceDa";
        String email = "vincedavince@163.com";
        Contact contact = new Contact(author, url, email);
        return new ApiInfoBuilder()
                .title("ou-system接口文档")
                .description("接口文档")
                .contact(contact)
                .version("1.0")
                .build();
    }

}
*/
