package com.ou.common.exception.filter;

import java.io.IOException;

import com.ou.common.exception.BadRequestException;
import com.ou.common.exception.UnknownRequestException;
import org.springframework.security.access.AccessDeniedException;
import javax.servlet.*;

import org.springframework.stereotype.Component;

import com.ou.common.exception.NotLoggedInException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author vince
 * @date 2019/11/11 23:14
 */

@Slf4j
@Component
public class ExceptionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
        try {
            chain.doFilter(request, response);
        } catch (NotLoggedInException e) {
            log.error(e.getMessage());
            try {
                request.getRequestDispatcher("/api/v1/auth/notLoggedIn").forward(request, response);
            } catch (ServletException ex) {
                ex.printStackTrace();
            }
        }  catch (UnknownRequestException e) {
            log.error(e.getMessage());
            try {
                request.getRequestDispatcher("/api/v1/auth/unknownRequest").forward(request, response);
            } catch (ServletException ex) {
                ex.printStackTrace();
            }
        } catch (AccessDeniedException e) {
            log.error(e.getMessage());
            try {
                request.getRequestDispatcher("/api/v1/auth/accessDenied").forward(request, response);
            } catch (ServletException ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
