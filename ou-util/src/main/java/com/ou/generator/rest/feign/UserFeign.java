package com.ou.generator.rest.feign;

import com.ou.common.domain.system.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ou.generator.config.FeignConfiguration;

/**
 *  configuration: 读取配置文件, 设置头部
 * @author vince
 * @date 2019/12/17 16:56
 */
@FeignClient(value = "ou-system", configuration = FeignConfiguration.class)
public interface UserFeign {

    /**
     *  根据用户名获取用户信息
     * @param username 用户名
     * @return 符合条件的用户信息
     */
    @GetMapping(value = "/api/v1/system/users/getByUsername")
    User getByUsername(@RequestParam(value = "username") String username);

}
