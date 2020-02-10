package com.ou.common.exception.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import com.ou.common.exception.UnknownRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ou.common.exception.BadRequestException;
import com.ou.common.exception.NotLoggedInException;
import com.ou.common.response.FailedResponse;

import lombok.extern.slf4j.Slf4j;

/**
 *  统一异常处理
 * @author vince
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     *  处理未知异常
     * @param e  未知异常信息
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Throwable e) {
        e.printStackTrace();
        log.error(e.getMessage());
        FailedResponse failedResponse = new FailedResponse(BAD_REQUEST.value(), e.getMessage());
        return buildErrorResponse(failedResponse);
    }

    /**
     *  处理自定义异常
     * @param e  自定义异常信息
     * @return
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity handleBadRequestException(BadRequestException e) {
        log.error(e.getMessage());
        FailedResponse failedResponse = new FailedResponse(e.getStatus(), e.getMessage());
        return buildErrorResponse(failedResponse);
    }

    /**
     *  处理未知请求异常
     * @param e  自定义异常信息
     * @return
     */
    @ExceptionHandler(UnknownRequestException.class)
    public ResponseEntity handleUnknownRequestException(UnknownRequestException e) {
        log.error(e.getMessage());
        FailedResponse failedResponse = new FailedResponse(e.getStatus(), e.getMessage());
        return buildErrorResponse(failedResponse);
    }

    /**
     *  处理参数校验异常
     * @param e  参数校验异常信息
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        List<String> errorMessages = new ArrayList<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError single : fieldErrors) {
            errorMessages.add(single.getDefaultMessage());
        }
        FailedResponse failedResponse = new FailedResponse(BAD_REQUEST.value(), errorMessages.toString());
        return buildErrorResponse(failedResponse);
    }

    /**
     *  处理未登录异常
     * @param e  未登录异常信息
     * @return
     */
    @ExceptionHandler(NotLoggedInException.class)
    public ResponseEntity handleNotLoggedInException(NotLoggedInException e) {
        log.error(e.getMessage());
        FailedResponse failedResponse = new FailedResponse(e.getStatus(), e.getMessage());
        return buildErrorResponse(failedResponse);
    }


    /**
     *  处理权限不足异常
     * @param e  自定义异常信息
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity handleAccessDeniedException(AccessDeniedException e) {
        log.error(e.getMessage());
        FailedResponse failedResponse = new FailedResponse(UNAUTHORIZED.value(), e.getMessage());
        return buildErrorResponse(failedResponse);
    }

    /**
     * 权限异常
     *
     * @return
     */
 /*   @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity authorizationException(AuthorizationException e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(UNAUTHORIZED.value(), "权限不足");
        return buildErrorResponse(errorResponse);
    }*/

    /**
     * 统一返回
     * @param failedResponse 异常实体类
     * @return
     */
    private ResponseEntity buildErrorResponse(FailedResponse failedResponse) {
        return ResponseEntity.status(failedResponse.getStatus()).body(failedResponse);
    }
}
