package com.ou.system.security.rest;

import com.ou.common.exception.NotLoggedInException;
import com.ou.common.exception.UnknownRequestException;
import com.ou.system.security.domain.AuthorizationUser;
import com.ou.system.security.domain.JwtUser;
import com.ou.system.security.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
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
        log.info("token isExpired: {}", jwtTokenUtil.isTokenExpired(token));
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
    @RequestMapping(value = "/notLoggedIn")
    @ResponseBody
    public ResponseEntity<String> notLoggedIn() {
        throw new NotLoggedInException("请先登录");
    }

    /**
     * 未知请求(多指绕过网关发起的请求)
     *
     * @return ResponseEntity
     */
    @RequestMapping(value = "/unknownRequest")
    @ResponseBody
    public ResponseEntity<String> unknownRequest() {
        throw new UnknownRequestException("未知请求");
    }

    /**
     * 未登录
     *
     * @return ResponseEntity
     */
    @RequestMapping(value = "/accessDenied")
    @ResponseBody
    public ResponseEntity<String> accessDenied() {
        throw new AccessDeniedException("权限不足");
    }

}
