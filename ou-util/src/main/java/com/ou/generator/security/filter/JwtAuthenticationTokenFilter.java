package com.ou.generator.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ou.common.exception.UnknownRequestException;
import com.ou.generator.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ou.common.exception.NotLoggedInException;
import com.ou.generator.security.util.JwtTokenUtil;

import cn.hutool.core.util.StrUtil;

/**
 * @author vince
 * @date 2019/12/10 20:51security
 */
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    @Qualifier("jwtUserDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Value("${jwt.login.path}")
    private String loginPath;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {
        // 验证是否请求是否是从网关转发, 不是则抛出异常
        boolean isLegalRequest = false;
        String secretKey = request.getHeader("secretKey");
        if (StrUtil.isNotBlank(secretKey)) {
            String key = (String)redisUtil.get("secretKey");
            if (StrUtil.isNotBlank(key) && secretKey.equals(key)) {
                isLegalRequest = true;
            }
        }
        if (!isLegalRequest) {
            log.error("empty secretKey: {}", secretKey);
            throw new UnknownRequestException("未知请求");
        }
        String token = request.getHeader(jwtTokenUtil.getHeader());
        if (!StrUtil.isEmpty(token)) {
            String username = jwtTokenUtil.getUsernameFromToken(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtTokenUtil.validateToken(token, userDetails)) {
                    // 将用户信息存入 authentication，方便后续校验
                    UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // 将 authentication 存入 ThreadLocal，方便后续获取用户信息
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } else {
            // 登录接口允许token为空
            String uri = request.getRequestURI();
            if (!loginPath.equals(uri)) {
                throw new NotLoggedInException("请先登录");
            }
        }
        chain.doFilter(request, response);
    }

}
