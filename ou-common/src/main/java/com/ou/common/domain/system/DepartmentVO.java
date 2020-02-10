package com.ou.common.domain.system;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * @author vince
 * @date 2019/10/11 23:46
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepartmentVO {

    /**
     *  部门id
     */
    private Long id;

    /**
     *  父级部门id
     */
    private Long parentId;

    /**
     * 部门名称
     */
    private String name;

    /**
     *  部门描述
     */
    private String description;

    /**
     * 是否启用: 0否, 1是
     */
    private Boolean enable;

    /**
     *  创建时间
     */
    private LocalDateTime createTime;

    /**
     *  子节点
     */
    private List<DepartmentVO> children;

    public interface Insert{};

    public interface Update{};
}
