package com.ou.generator.domain.query;

import org.springframework.data.jpa.domain.Specification;

import com.ou.common.base.BaseQuery;
import com.ou.common.retention.QueryField;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author vince
 * @date 2019/12/6 16:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GeneratorFieldQueryCriteria extends BaseQuery<GeneratorFieldQueryCriteria> {

    /**
     * 模糊查询条件: 字段名
     */
    @QueryField(blurry = {"name"})
    private String blurry;

    /**
     *  表id
     */
    @QueryField(column = "tableId")
    private Long tableId;

    /**
     *  创建人id
     */
    @QueryField(column = "createUserId")
    private Long createUserId;

    @Override
    public Specification<GeneratorFieldQueryCriteria> toSpecification() {
        return super.toSpecificationWithAnd();
    }

}
