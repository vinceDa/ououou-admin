package com.ou.system.domain.query;

import com.ou.common.base.BaseQuery;
import com.ou.common.constant.sql.ConnectConditionEnum;
import com.ou.common.retention.QueryField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author vince
 * @date 2019/10/15 11:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuQueryCriteria extends BaseQuery<MenuQueryCriteria> {

    /**
     *  模糊查询条件: 菜单名/菜单路径
     */
    @QueryField(blurry = {"name", "path"}, connect = ConnectConditionEnum.LIKE)
    private String blurry;

    /**
     *  是否显示
     */
    @QueryField(column = "show", connect = ConnectConditionEnum.EQUAL)
    private Boolean show;

    /**
     *  创建人id
     */
    @QueryField(column = "createUserId")
    private Long createUserId;

    @Override
    public Specification<MenuQueryCriteria> toSpecification() {
        return super.toSpecificationWithAnd();
    }
}
