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
public class GeneratorTableQueryCriteria extends BaseQuery<GeneratorTableQueryCriteria> {

    /**
     * 模糊查询条件: 表名
     */
    @QueryField(blurry = {"name"})
    private String blurry;

    /**
     *  数据库id
     */
    @QueryField(column = "databaseId")
    private Long databaseId;

    /**
     *  创建人id
     */
    @QueryField(column = "createUserId")
    private Long createUserId;

    @Override
    public Specification<GeneratorTableQueryCriteria> toSpecification() {
        return super.toSpecificationWithAnd();
    }

}
