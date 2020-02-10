package com.ou.system.security.service.impl;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ou.common.exception.BadRequestException;
import com.ou.system.domain.Role;
import com.ou.system.domain.User;
import com.ou.system.repository.RoleRepository;
import com.ou.system.repository.UserRepository;
import com.ou.system.security.domain.JwtUser;

import cn.hutool.core.util.StrUtil;

/**
 * @author vince
 * @date 2019/12/11 11:01
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getByUsername(username);
        if (user == null) {
            throw new BadRequestException("用户不存在");
        }
        return this.createJwtUser(user);
    }

    public Collection<GrantedAuthority> mapToGrantedAuthorities(User user) {
        return user.getRoles().stream()
            .flatMap(role -> role.getMenus().stream().filter(single -> StrUtil.isNotEmpty(single.getPermissionTag())))
            .map(menu -> new SimpleGrantedAuthority(menu.getPermissionTag())).collect(Collectors.toList());
    }

    public UserDetails createJwtUser(User user) {
        return new JwtUser(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getPhone(),
            user.getDepartmentId(), user.getJobId(), this.mapToGrantedAuthorities(user), user.getEnable(),
            user.getCreateTime(), user.getLastPasswordResetTime());
    }
}
