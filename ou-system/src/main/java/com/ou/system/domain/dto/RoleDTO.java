package com.ou.system.domain.dto;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.ou.system.domain.Menu;
import com.ou.system.domain.User;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vince
 */
@Getter
@Setter
public class RoleDTO {

    /**
     *  主键id
     */
    @NotNull(groups = Update.class, message = "id不能为空")

    private Long id;

    /**
     *  用户名
     */
    @NotNull(groups = Insert.class, message = "角色名不能为空")
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
     *  更新时间
     */

    private LocalDateTime updateTime;

    /**
     *  拥有当前角色的用户的集合
     */

    private Set<User> users;

    /**
     *  当前角色拥有的菜单的集合
     */

    private Set<Menu> menus;

    @Override
    public String toString() {
        return "RoleDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    public @interface Insert{}

    public @interface Update{}
}
