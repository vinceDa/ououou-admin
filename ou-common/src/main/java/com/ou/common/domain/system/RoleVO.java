package com.ou.common.domain.system;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * @author vince
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleVO {

    /**
     *  角色id
     */
    private String id;

    /**
     *  角色名
     */
    private String name;

    /**
     *  描述
     */
    private String description;

    /**
     *  创建时间
     */
    private LocalDateTime createTime;

    /**
     *  当前角色拥有的菜单的集合
     */
    private Set<MenuVO> menus;

}
