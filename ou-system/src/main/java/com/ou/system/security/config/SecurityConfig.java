package com.ou.system.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.ou.common.exception.filter.ExceptionFilter;
import com.ou.system.security.filter.JwtAuthenticationTokenFilter;
import com.ou.system.security.handler.EntryPointUnauthorizedHandler;
import com.ou.system.security.handler.MyAuthenticationFailureHandler;
import com.ou.system.security.handler.MyAuthenticationSuccessHandler;
import com.ou.system.security.handler.RestAccessDeniedHandler;

/**
 * Spring Security 配置类 注: @EnableGlobalMethodSecurity 开启注解的权限控制，默认是关闭的。
 * prePostEnabled：使用表达式实现方法级别的控制，如：// @PreAuthorize("hasRole('ADMIN')") securedEnabled: 开启 @Secured
 * 注解过滤权限，如：@Secured("ROLE_ADMIN") jsr250Enabled: 开启 @RolesAllowed 注解过滤权限，如：@RolesAllowed("ROLE_ADMIN")
 * 
 * @author vince
 * @date 2019/12/10 20:27
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("jwtUserDetailsServiceImpl")
    private UserDetailsService userDetailsService;

/*    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;*/

    @Autowired
    private EntryPointUnauthorizedHandler entryPointUnauthorizedHandler;

    @Autowired
    private RestAccessDeniedHandler restAccessDeniedHandler;

    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    /**
     * 从容器中取出 AuthenticationManagerBuilder，执行方法里面的逻辑之后，放回容器
     */
    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // ExceptionFilter 处理过滤器抛出的异常
        http.addFilterBefore(new ExceptionFilter(), UsernamePasswordAuthenticationFilter.class);
        // 在UsernamePasswordAuthenticationFilter 之前添加 JwtAuthenticationTokenFilter
        http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
            .antMatchers(
                    HttpMethod.GET,
                    "/*.html",
                    "/**/*.html",
                    "/**/*.css",
                    "/**/*.js"
            ).anonymous()
            .antMatchers( HttpMethod.POST,"/api/v1/login").anonymous()
            /*.antMatchers("/api/vi/**").authenticated()*/;

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry =
            http.authorizeRequests();
        // 让Spring security 放行所有preflight request（cors 预检请求）
        registry.requestMatchers(CorsUtils::isPreFlightRequest).permitAll();
        // 处理异常情况：认证失败和权限不足
        http.exceptionHandling().authenticationEntryPoint(entryPointUnauthorizedHandler)
            .accessDeniedHandler(restAccessDeniedHandler);
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowCredentials(true);
        cors.addAllowedOrigin("*");
        cors.addAllowedHeader("*");
        cors.addAllowedMethod("*");
        configurationSource.registerCorsConfiguration("/**", cors);
        return new CorsFilter(configurationSource);
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}