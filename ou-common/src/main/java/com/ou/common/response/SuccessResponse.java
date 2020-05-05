package com.ou.common.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

/**
 * @author vince
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SuccessResponse extends BaseResponse {

    public SuccessResponse() {
        setStatus(HttpStatus.OK.value());
        setMsg("success");
    }

    public SuccessResponse(String msg) {
        setStatus(HttpStatus.OK.value());
        setMsg(msg);
    }

    public SuccessResponse(Object data) {
        setStatus(HttpStatus.OK.value());
        setMsg("success");
        setData(data);
    }

    public SuccessResponse(String msg, Object data) {
        setStatus(HttpStatus.OK.value());
        setMsg(msg);
        setData(data);
    }
}
