package com.ou.common.response;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * 异常信息实体
 *
 * @author vince
 */
@Data
public class BaseResponse implements Serializable {

    private Integer status;

    private String msg;

    private Object data;

    private static final long serialVersionUID = 1L;

    public BaseResponse() {
        this.status = HttpStatus.OK.value();
        this.msg = "success";
    }

    public BaseResponse(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public BaseResponse(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
}
