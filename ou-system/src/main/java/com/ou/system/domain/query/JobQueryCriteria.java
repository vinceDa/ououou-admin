package com.ou.system.domain.query;

import com.ou.common.base.BaseQuery;
import com.ou.common.retention.QueryField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.Specification;

/**
 *  岗位查询参数实体类
 * @author vince
 * @date 2019/10/12 16:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JobQueryCriteria extends BaseQuery<JobQueryCriteria> {

    /**
     *  模糊查询条件: 岗位名
     */
    // @ApiModelProperty(name = "模糊查询条件: 岗位名")
    @QueryField(blurry = {"name"})
    private String blurry;

    /**
     *  部门id
     */
    // @ApiModelProperty(name = "部门id")
    @QueryField(column = "department_id")
    private Long departmentId;

    /**
     *  是否启用: 0否, 1是
     */
    // @ApiModelProperty(name = "是否启用")
    @QueryField(column = "enable")
    private Boolean enable;

    /**
     *  创建人id
     */
    @QueryField(column = "createUserId")
    private Long createUserId;

    @Override
    public Specification<JobQueryCriteria> toSpecification() {
        return super.toSpecificationWithAnd();
    }
}
