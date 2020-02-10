package com.ou.system.security.domain;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Zheng Jie
 * @date 2018-11-30
 */
@Getter
@Setter
public class AuthorizationUser {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String code;

    private String uuid = "";

}
