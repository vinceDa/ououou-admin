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
public class GeneratorDatabaseQueryCriteria extends BaseQuery<GeneratorDatabaseQueryCriteria> {

    /**
     * 模糊查询条件: 数据库名
     */
    @QueryField(blurry = {"name"})
    private String blurry;

    /**
     *  数据库连接id
     */
    @QueryField(column = "connectionId")
    private Long connectionId;

    /**
     *  创建人id
     */
    @QueryField(column = "createUserId")
    private Long createUserId;

    @Override
    public Specification<GeneratorDatabaseQueryCriteria> toSpecification() {
        return super.toSpecificationWithAnd();
    }

}
