package com.ou.quartz.domain.query;

import com.ou.common.base.BaseQuery;
import com.ou.common.retention.QueryField;
import org.springframework.data.jpa.domain.Specification;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author vince
 * @date 2020/02/02 07:24:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuartzTaskQueryCriteria extends BaseQuery<QuartzTaskQueryCriteria> {

    /**
     * 模糊查询条件
     */
    @QueryField(blurry = {"name"})
    private String blurry;

    /**
     * 查询条件, example: entity中对应的key(key对应到具体的某个字段)
     */
    @QueryField(column = "example")
    private Boolean queryField;

    @Override
    public Specification<QuartzTaskQueryCriteria> toSpecification() {
        return super.toSpecificationWithAnd();
    }
}
