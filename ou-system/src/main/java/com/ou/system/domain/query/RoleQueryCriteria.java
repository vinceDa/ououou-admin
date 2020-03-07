package com.ou.system.domain.query;

import org.springframework.data.jpa.domain.Specification;

import com.ou.common.base.BaseQuery;
import com.ou.common.retention.QueryField;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *  角色查询参数实体类
 * @author vince
 * @date 2019/10/12 16:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleQueryCriteria extends BaseQuery<RoleQueryCriteria> {

    /**
     *  模糊查询条件: 角色名
     */
     @QueryField(blurry = {"name"})
    private String blurry;

    /**
     *  创建人id
     */
    @QueryField(column = "createUserId")
    private Long createUserId;

    @Override
    public Specification<RoleQueryCriteria> toSpecification() {
        return super.toSpecificationWithAnd();
    }
}
