package com.ou.system.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author vince
 * @date 2019/10/11 23:46
 */
@Data
public class DepartmentDTO {

    /**
     *  主键id
     */
    @NotNull(groups = Update.class, message = "id不能为空")
    private Long id;

    /**
     *  父级部门id
     */
    private Long parentId;

    /**
     * 部门名称
     */
    @NotNull(groups = Insert.class, message = "部门名称不能为空")
    private String name;

    /**
     * 是否启用: 0否, 1是
     */
    @NotNull(groups = Insert.class, message = "部门状态不能为空")
    private Boolean enable;

    /**
     *  部门描述
     */
    private String description;

    /**
     *  子节点
     */
    private List<DepartmentDTO> children;

    /**
     *  创建时间
     */
    private LocalDateTime createTime;

    /**
     *  更新时间
     */
    private LocalDateTime updateTime;

    public interface Insert{};

    public interface Update{};
}
