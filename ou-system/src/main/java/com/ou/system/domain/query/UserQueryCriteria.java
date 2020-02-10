package com.ou.system.domain.query;

import com.ou.common.base.BaseQuery;
import com.ou.common.retention.QueryField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.Specification;

/**
 *  用户查询参数实体类
 * @author vince
 * @date 2019/10/12 16:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryCriteria extends BaseQuery<UserQueryCriteria> {

    /**
     *  模糊查询条件: 用户名/邮箱/手机号码
     */
    // @ApiModelProperty(value = "模糊查询条件: 用户名/邮箱/手机号码")
    @QueryField(blurry = {"username", "email", "phone"})
    private String blurry;

    /**
     *  是否启用
     */
    @QueryField(column = "enable")
    // @ApiModelProperty(value = "是否启用: 0否, 1是")
    private Boolean enable;

    /**
     *  部门id
     */
    @QueryField(column = "departmentId")
    // @ApiModelProperty(value = "部门id")
    private Long departmentId;

    /**
     *  创建人id
     */
    @QueryField(column = "createUserId")
    private Long createUserId;

    @Override
    public Specification<UserQueryCriteria> toSpecification() {
        return super.toSpecificationWithAnd();
    }
}
