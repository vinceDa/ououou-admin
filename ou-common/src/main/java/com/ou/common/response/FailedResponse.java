package com.ou.common.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * 异常信息实体
 *
 * @author vince
 */
@Data
public class FailedResponse extends BaseResponse {

    public FailedResponse() {
        setStatus(HttpStatus.BAD_REQUEST.value());
        setMsg("failed");
    }

    public FailedResponse(String msg) {
        setStatus(HttpStatus.BAD_REQUEST.value());
        setMsg(msg);
    }

    public FailedResponse(Object data) {
        setStatus(HttpStatus.BAD_REQUEST.value());
        setMsg("failed");
        setData(data);
    }

    public FailedResponse(Integer status, String msg) {
        setStatus(status);
        setMsg(msg);
    }

    public FailedResponse(String msg, Object data) {
        setStatus(HttpStatus.BAD_REQUEST.value());
        setMsg(msg);
        setData(data);
    }
}
