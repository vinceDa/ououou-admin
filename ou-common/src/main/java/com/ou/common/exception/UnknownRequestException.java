package com.ou.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 *  未知请求异常处理
 * @author vince
 */
@Getter
public class UnknownRequestException extends RuntimeException {

    private Integer status = 400;

    public UnknownRequestException(String message) {
        super(message);
    }

    public UnknownRequestException(HttpStatus status, String message) {
        super(message);
        this.status = status.value();
    }
}
