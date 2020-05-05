package com.ou.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ou.common.base.BaseDO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * @author vince
 */
@EqualsAndHashCode(callSuper = true, exclude = {"users"})
@Entity
@Getter
@Setter
@Table(name = "system_role")
public class Role extends BaseDO {

    /**
     *  角色名
     */
    @Column(name = "name")
    private String name;

    /**
     *  描述
     */
    @Column(name = "description")
    private String description;

    /**
     *  拥有当前角色的用户的集合
     *  添加@JsonIgnore注解是因为在获取用户信息时将roles信息一并查出, 而role中亦包含user信息, json解析时会出现无限递归的现象
     */
    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    /**
     *  当前角色拥有的菜单的集合
     */
    @ManyToMany
    @JoinTable(name = "system_role_menu", joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "menu_id", referencedColumnName = "id")})
    private Set<Menu> menus;

    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
