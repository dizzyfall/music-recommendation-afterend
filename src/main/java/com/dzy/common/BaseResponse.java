package com.dzy.common;

import com.dzy.constant.StatusCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/5  23:44
 */
@Data
public class BaseResponse<T> implements Serializable {

    /**
     * 状态码
     */
    private final String code;

    /**
     * 返回信息
     */
    private final String msg;

    /**
     * 自定义响应详情
     */
    private final String description;

    /**
     * 返回对象
     */
    private T data;

    public BaseResponse(StatusCode statusCode) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
        this.description = statusCode.getDesciption();
    }

    public BaseResponse(StatusCode statusCode, T data) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
        this.description = statusCode.getDesciption();
        this.data = data;
    }

    /**
     * 自定义响应详情，有返回数据
     *
     * @param statusCode
     * @param data
     * @param description
     */
    public BaseResponse(StatusCode statusCode, T data, String description) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
        this.description = description;
        this.data = data;
    }

    /**
     * 自定义响应详情，无返回数据
     *
     * @param statusCode
     * @param description
     */
    public BaseResponse(StatusCode statusCode, String description) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
        this.description = description;
    }

    /**
     * 自定义响应状态码，有返回数据
     *
     * @param code
     * @param msg
     * @param description
     * @param data
     */
    public BaseResponse(String code, String msg, String description, T data) {
        this.code = code;
        this.msg = msg;
        this.description = description;
        this.data = data;
    }

    /**
     * 自定义响应状态码，无返回数据
     *
     * @param code
     * @param msg
     * @param description
     */
    public BaseResponse(String code, String msg, String description) {
        this.code = code;
        this.msg = msg;
        this.description = description;
    }

}
