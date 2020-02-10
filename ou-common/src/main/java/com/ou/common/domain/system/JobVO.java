package com.ou.common.domain.system;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * @author vince
 * @date 2019/10/11 23:46
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobVO {

    /**
     *  主键id
     */
    private Long id;

    /**
     * 岗位名称
     */
    private String name;


    /**
     *  是否启用: 0否, 1是
     */
    private Boolean enable;

    /**
     *  所属部门id
     */
    private Long departmentId;

    /**
     *  所属部门名称
     */
    private String departmentName;

    /**
     *  创建时间
     */
    private LocalDateTime createTime;

}
