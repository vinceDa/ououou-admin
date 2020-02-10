package com.ou.generator.domain.query;

import com.ou.common.base.BaseQuery;
import com.ou.common.retention.QueryField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author vince
 * @date 2019/12/6 16:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GeneratorConnectionQueryCriteria extends BaseQuery<GeneratorConnectionQueryCriteria> {

    /**
     * 模糊查询条件: 连接名, 主机名
     */
    @QueryField(blurry = {"name", "host"})
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
    public Specification<GeneratorConnectionQueryCriteria> toSpecification() {
        return super.toSpecificationWithAnd();
    }

}
