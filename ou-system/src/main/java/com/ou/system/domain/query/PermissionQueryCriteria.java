package com.ou.system.domain.query;

import org.springframework.data.jpa.domain.Specification;

import com.ou.common.base.BaseQuery;
import com.ou.common.retention.QueryField;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author vince
 * @date 2019/10/15 11:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PermissionQueryCriteria extends BaseQuery<PermissionQueryCriteria> {

    /**
     * 模糊查询条件: 权限名
     */
    @QueryField(blurry = {"name"})
    private String blurry;

    /**
     *  创建人id
     */
    @QueryField(column = "createUserId")
    private Long createUserId;

    @Override
    public Specification<PermissionQueryCriteria> toSpecification() {
        return super.toSpecificationWithAnd();
    }
}
