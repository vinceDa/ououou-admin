package com.ou.generator.security.domain;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author vince
 * @date 2019/12/11 10:42
 */
@Getter
@AllArgsConstructor
public class JwtUser implements UserDetails {

    @JsonIgnore
    private final Long id;

    private final String username;

    private final String password;

   // private final String avatar;

    private final String email;

    private final String phone;

    private final Long departmentId;

    private final Long jobId;

    @JsonIgnore
    private final Collection<? extends GrantedAuthority> authorities;

    private final boolean enabled;

    private LocalDateTime createTime;

    @JsonIgnore
    private final LocalDateTime lastPasswordResetTime;

    /**
     *  账户是否未过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账户是否未被锁
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Collection getRoles() {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }
}
