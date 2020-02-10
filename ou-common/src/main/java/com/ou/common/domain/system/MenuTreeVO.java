package com.ou.common.domain.system;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * @author vince
 * @date 2019/11/21 19:29
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuTreeVO {

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
     *  组件路径
     */
    private String component;

    /**
     *  菜单图标代码
     */
    private String icon;

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
    private List<MenuTreeVO> children;


}
