package com.ou.system.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author vince
 * @date 2019/10/11 21:28
 */
@Data
public class MenuDTO {

    /**
     *  主键id
     */
    @NotNull(groups = Update.class, message = "菜单id不能为空")

    private Long id;

    /**
     *  上级菜单id
     */
    private Long parentId;

    /**
     *  菜单名
     */
    @NotEmpty(groups = Insert.class, message = "菜单名不能为空")
    private String name;

    /**
     *  菜单类型: 0: 目录, 1: 菜单, 2: 按钮
     */
    @NotNull(groups = Insert.class, message = "菜单类型不能为空")
    private String type;

    /**
     *  菜单描述
     */
    private String description;

    /**
     *  权限标识
     */
    private String permissionTag;

    /**
     *  路由
     */
    private String path;

    /**
     *  菜单图标代码
     */
    private String icon;

    /**
     *  是否显示: 0否, 1是
     */
    private Boolean show;

    /**
     *  菜单排列序号
     */
    @NotNull(groups = Insert.class, message = "菜单排序不能为空")
    private Integer sort;

    /**
     *  创建时间
     */

    private LocalDateTime createTime;

    /**
     *  更新时间
     */

    private LocalDateTime updateTime;

    /**
     *  子节点
     */

    private List<MenuDTO> children;

    public interface Insert{};

    public interface Update{};
}
