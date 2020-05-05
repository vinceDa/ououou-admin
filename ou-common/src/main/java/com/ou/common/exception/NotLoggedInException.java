package com.ou.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 *  未登录异常处理
 * @author vince
 */
@Getter
public class NotLoggedInException extends RuntimeException {

    private Integer status = 530;

    public NotLoggedInException(String message) {
        super(message);
    }

    public NotLoggedInException(HttpStatus status, String message) {
        super(message);
        this.status = status.value();
    }
}
