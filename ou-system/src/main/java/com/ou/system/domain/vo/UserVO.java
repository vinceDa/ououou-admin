package com.ou.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author vince
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVO {

    /**
     *  主键id
     */
    private Long id;

    /**
     *  用户名
     */
    private String username;

    /**
     *  手机号码
     */
    private String phone;

    /**
     *  邮箱
     */
    private String email;

    /**
     *  是否启用: 0否, 1是
     */
    private Boolean enable;

    /**
     *  职位id
     */
    private Long jobId;

    /**
     *  职位名称
     */
    private String jobName;

    /**
     *  所属部门id
     */
    private Long departmentId;

    /**
     *  所属部门名称
     */
    private String departmentName;

    /**
     *  创建时间
     */
    private LocalDateTime createTime;

    /**
     *  上一次密码更新时间
     */
    private LocalDateTime lastPasswordResetTime;

    /**
     *  当前用户拥有的角色的集合
     */
    private Set<RoleVO> roles;

    @Override
    public String toString() {
        return "UserVO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", enable=" + enable +
                ", jobId=" + jobId +
                ", departmentId=" + departmentId +
                ", createTime=" + createTime +
                ", lastPasswordResetTime=" + lastPasswordResetTime +
                '}';
    }
}
