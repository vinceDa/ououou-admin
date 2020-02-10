package com.ou.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 *  统一异常处理
 * @author vince
 */
@Getter
public class BadRequestException extends RuntimeException {

    private Integer status = BAD_REQUEST.value();
    
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(HttpStatus status, String message) {
        super(message);
        this.status = status.value();
    }
}
