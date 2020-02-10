package com.ou.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ou.common.base.BaseDO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

/**
 * @author vince
 * @date 2019/10/11 21:28
 */
@EqualsAndHashCode(callSuper = true, exclude = "roles")
@Entity
@Getter
@Setter
@Table(name = "system_menu")
public class Menu extends BaseDO {

    /**
     *  上级菜单id
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     *  菜单名
     */
    @Column(name = "name")
    private String name;

    /**
     *  菜单类型: 0: 目录, 1: 菜单, 2: 按钮
     */
    @Column(name = "type")
    private String type;

    /**
     *  菜单描述
     */
    @Column(name = "description")
    private String description;

    /**
     *  路由
     */
    @Column(name = "path")
    private String path;

    /**
     *  菜单图标代码
     */
    @Column(name = "icon")
    private String icon;

    /**
     *  权限标识
     */
    @Column(name = "permission_tag")
    private String permissionTag;

    /**
     *  是否显示: 0否, 1是
     */
    @Column(name = "is_show")
    private Boolean show;

    /**
     *  菜单排列序号
     */
    @Column(name = "sort")
    private Integer sort;

    /**
     *  拥有当前菜单的角色的集合
     */
    @JsonIgnore
    @ManyToMany(mappedBy = "menus")
    private Set<Role> roles;

    @Override
    public String toString() {
        return "Menu{" +
                "parentId=" + parentId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", path='" + path + '\'' +
                ", icon='" + icon + '\'' +
                ", show=" + show +
                ", sort=" + sort +
                '}';
    }
}
