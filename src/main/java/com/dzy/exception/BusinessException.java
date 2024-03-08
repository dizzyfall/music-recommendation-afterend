package com.dzy.exception;

import com.dzy.constant.StatusCode;

/**
 * 自定义业务异常类
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/6  9:39
 */
public class BusinessException extends RuntimeException {
    /**
     * 状态码
     */
    private String code;

    /**
     * 自定义响应详情
     */
    private String description;

    public BusinessException(String message, String code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    /**
     * @param statusCode 响应状态码
     */
    public BusinessException(StatusCode statusCode) {
        super(statusCode.getMsg());
        this.code = statusCode.getCode();
        this.description = statusCode.getDesciption();
    }

    /**
     * 自定义响应详情
     *
     * @param statusCode  响应状态码
     * @param description 自定义响应详情
     */
    public BusinessException(StatusCode statusCode, String description) {
        super(statusCode.getMsg());
        this.code = statusCode.getCode();
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
