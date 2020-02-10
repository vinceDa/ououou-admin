package com.ou.system.domain;

import com.ou.common.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author vince
 * @date 2019/10/11 23:46
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "system_department")
public class Department extends BaseDO {

    /**
     *  父级部门id
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 部门名称
     */
    @Column(name = "name")
    private String name;

    /**
     *  部门描述
     */
    @Column(name = "description")
    private String description;

    /**
     *  是否启用: 0否, 1是
     */
    @Column(name = "is_enable")
    private Boolean enable;
}
