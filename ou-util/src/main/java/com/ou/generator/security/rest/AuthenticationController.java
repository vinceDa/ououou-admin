package com.ou.generator.security.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ou.common.exception.NotLoggedInException;
import com.ou.generator.security.domain.AuthorizationUser;
import com.ou.generator.security.domain.JwtUser;
import com.ou.generator.security.util.JwtTokenUtil;

import lombok.extern.slf4j.Slf4j;

/**
 *  授权、根据token获取用户详细信息
 * @author vince
 * @date 2019-12-11
 */
@Slf4j
@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    @Qualifier("jwtUserDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    /**
     * 登录授权
     * 
     * @param authorizationUser
     *            传入的用户信息
     * @return 成功返回token, 失败返回对应的错误信息
     */
    @PostMapping(value = "/login")
    public ResponseEntity login(@Validated @RequestBody AuthorizationUser authorizationUser) {

        JwtUser jwtUser = (JwtUser)userDetailsService.loadUserByUsername(authorizationUser.getUsername());

        if (!jwtUser.getPassword().equals(authorizationUser.getPassword())) {
            throw new AccountExpiredException("密码错误");
        }

        if (!jwtUser.isEnabled()) {
            throw new AccountExpiredException("账号已停用，请联系管理员");
        }

        // 生成令牌
        final String token = jwtTokenUtil.generateToken(jwtUser);

        // 返回 token
        return ResponseEntity.ok(token);
    }

    /**
     * 获取用户信息
     * 
     * @return
     */
    @GetMapping(value = "/getUserInfo")
    public ResponseEntity getUserInfo() {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JwtUser jwtUser = (JwtUser)userDetailsService.loadUserByUsername(userDetails.getUsername());
        return ResponseEntity.ok(jwtUser);
    }

    /**
     * 未登录
     *
     * @return ResponseEntity
     */
    @GetMapping(value = "/notLoggedIn")
    @ResponseBody
    public ResponseEntity<String> notLoggedIn() {
        throw new NotLoggedInException("请先登录");
    }

    /**
     * 未登录
     *
     * @return ResponseEntity
     */
    @GetMapping(value = "/accessDenied")
    @ResponseBody
    public ResponseEntity<String> accessDenied() {
        throw new AccessDeniedException("权限不足");
    }

}
