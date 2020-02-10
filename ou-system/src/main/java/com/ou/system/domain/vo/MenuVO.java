package com.ou.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author vince
 * @date 2019/10/11 21:28
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuVO {

    /**
     *  菜单id
     */
    private Long id;

    /**
     *  上级菜单id
     */
    private Long parentId;

    /**
     *  菜单名
     */
    private String name;

    /**
     *  菜单类型: 0: 目录, 1: 菜单, 2: 按钮
     */
    private String type;

    /**
     *  菜单描述
     */
    private String description;

    /**
     *  路由
     */
    private String path;

    /**
     *  菜单图标代码
     */
    private String icon;

    /**
     *  权限标识
     */
    private String permissionTag;

    /**
     *  是否显示
     */
    private Boolean show;

    /**
     *  菜单排列序号
     */
    private Integer sort;

    /**
     *  创建时间
     */
    private LocalDateTime createTime;


    /**
     * 子菜单
     */
    private List<MenuVO> children;


}
