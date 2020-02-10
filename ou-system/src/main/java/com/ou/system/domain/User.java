package com.ou.system.domain;

import com.ou.common.base.BaseDO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author vince
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@Table(name = "system_user")
public class User extends BaseDO {

    /**
     *  用户名
     */
    @Column(name = "username")
    private String username;

    /**
     *  密码
     */
    @Column(name = "password")
    private String password;

    /**
     *  手机号码
     */
    @Column(name = "phone")
    private String phone;

    /**
     *  邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     *  是否启用: 0否, 1是
     */
    @Column(name = "is_enable")
    private Boolean enable;

    /**
     *  职位id
     */
    @Column(name = "job_id")
    private Long jobId;

    /**
     *  所属部门id
     */
    @Column(name = "department_id")
    private Long departmentId;

    /**
     *  上一次密码更新时间
     */
    @Column(name = "last_password_reset_time")
    private LocalDateTime lastPasswordResetTime;

    /**
     *  当前用户拥有的角色的集合
     */
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "system_user_role", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Set<Role> roles;

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", enable=" + enable +
                ", jobId=" + jobId +
                ", departmentId=" + departmentId +
                ", lastPasswordResetTime=" + lastPasswordResetTime +
                '}';
    }
}
