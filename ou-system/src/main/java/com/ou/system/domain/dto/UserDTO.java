package com.ou.system.domain.dto;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.ou.system.domain.Role;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vince
 */
@Getter
@Setter
public class UserDTO {

    /**
     *  主键id
     */
    @NotNull(groups = Update.class, message = "id不能为空")
    private Long id;

    /**
     *  用户名
     */
    @NotNull(groups = Insert.class, message = "用户名不能为空")
    private String username;

    /**
     *  密码
     */
    private String password;

    /**
     *  盐
     */

    private String salt;

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
     *  所属部门id
     */
    private Long departmentId;

    /**
     *  创建时间
     */
    private LocalDateTime createTime;

    /**
     *  更新时间
     */
    private LocalDateTime updateTime;

    /**
     *  上一次密码更新时间
     */
    private LocalDateTime lastPasswordResetTime;

    /**
     *  当前用户拥有的角色的集合
     */
    private Set<Role> roles;

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", enable=" + enable +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", lastPasswordResetTime=" + lastPasswordResetTime +
                ", roles=" + roles +
                '}';
    }

    public @interface Insert{}

    public @interface Update{}
}
