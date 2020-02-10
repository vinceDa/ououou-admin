package com.ou.common.domain.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ou.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author vince
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "system_job")
public class Job extends BaseDO {

    /**
     *  岗位名称
     */
    @Column(name = "name")
    private String name;

    /**
     *  是否启用: 0否, 1是
     */
    @Column(name = "is_enable")
    private Boolean enable;

    /**
     *  所属部门id
     */
    @Column(name = "department_id")
    private Long departmentId;

}
