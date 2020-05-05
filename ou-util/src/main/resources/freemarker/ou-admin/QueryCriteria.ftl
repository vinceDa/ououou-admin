package ${package}.domain.query;

import org.springframework.data.jpa.domain.Specification;

import ${package}.common.base.BaseQuery;
import ${package}.common.retention.QueryField;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ${author}
 * @date ${date}
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ${entityName}QueryCriteria extends BaseQuery<${entityName}QueryCriteria> {

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
    public Specification<${entityName}QueryCriteria> toSpecification() {
        return super.toSpecificationWithAnd();
    }
}
