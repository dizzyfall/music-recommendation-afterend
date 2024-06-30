package com.dzy.exception;

import com.dzy.common.BaseResponse;
import com.dzy.commonutils.ResponseUtil;
import com.dzy.constant.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/6  9:39
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler implements Serializable {
    private static final long serialVersionUID = -6029971277043738712L;

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("businessException:" + e.getMessage(), e);
        return ResponseUtil.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return ResponseUtil.error(StatusCode.SYSTEM_ERROR);
    }
}
