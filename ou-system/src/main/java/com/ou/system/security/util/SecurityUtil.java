package com.ou.system.security.util;

import com.ou.common.exception.NotLoggedInException;
import com.ou.system.security.domain.JwtUser;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author vince
 * @date 2019/12/11 20:34
 */
public class SecurityUtil {

    public static JwtUser getJwtUserInfo() {
        try {
            return (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotLoggedInException("请先登录");
        }
    }

    public static Long getJwtUserId() {
        try {
            JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return jwtUser.getId();
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotLoggedInException("请先登录");
        }
    }

}
