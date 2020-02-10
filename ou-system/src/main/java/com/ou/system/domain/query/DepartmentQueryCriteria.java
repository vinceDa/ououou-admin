package com.ou.system.domain.query;

import com.ou.common.base.BaseQuery;
import com.ou.common.retention.QueryField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author vince
 * @date 2019/10/11 23:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DepartmentQueryCriteria extends BaseQuery<DepartmentQueryCriteria> {

    /**
     * 模糊查询条件: 部门名称
     */
    @QueryField(blurry = {"name"})
    private String blurry;

    /**
     * 是否启用: 0否, 1是
     */
    @QueryField(column = "enable")
    private Boolean enable;

    /**
     *  创建人id
     */
    @QueryField(column = "createUserId")
    private Long createUserId;

    @Override
    public Specification<DepartmentQueryCriteria> toSpecification() {
        return super.toSpecificationWithAnd();
    }
}
