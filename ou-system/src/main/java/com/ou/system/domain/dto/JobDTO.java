package com.ou.system.domain.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author vince
 * @date 2019/10/14 23:21
 */
@Data
public class JobDTO {

    /**
     *  主键id
     */
    @NotNull(groups = Update.class, message = "id不能为空")
    private Long id;

    /**
     * 岗位名称
     */
    @NotNull(groups = Insert.class, message = "岗位名称不能为空")
    private String name;


    /**
     *  是否启用: 0否, 1是
     */
    @NotNull(groups = Insert.class, message = "岗位状态不能为空")
    private Boolean enable;

    /**
     *  所属部门id
     */
    private Long departmentId;

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
