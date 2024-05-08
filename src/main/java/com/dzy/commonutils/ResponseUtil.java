package com.dzy.commonutils;

import com.dzy.common.BaseResponse;
import com.dzy.constant.StatusCode;

/**
 * 返回工具类
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/6  9:53
 */
public class ResponseUtil {
    //成功
    public static <T> BaseResponse<T> success(StatusCode statusCode) {
        return new BaseResponse<>(statusCode);
    }

    public static <T> BaseResponse<T> success(StatusCode statusCode, String description) {
        return new BaseResponse<>(statusCode, description);
    }

    public static <T> BaseResponse<T> success(StatusCode statusCode, T data) {
        return new BaseResponse<>(statusCode, data);
    }

    public static <T> BaseResponse<T> success(StatusCode statusCode, T data, String description) {
        return new BaseResponse<>(statusCode, data, description);
    }

    //失败
    public static <T> BaseResponse<T> error(StatusCode statusCode) {
        return new BaseResponse<>(statusCode);
    }

    public static <T> BaseResponse<T> error(StatusCode statusCode, String description) {
        return new BaseResponse<>(statusCode, description);
    }

    public static <T> BaseResponse<T> error(StatusCode statusCode, T data) {
        return new BaseResponse<>(statusCode, data);
    }

    public static <T> BaseResponse<T> error(StatusCode statusCode, T data, String description) {
        return new BaseResponse<>(statusCode, data, description);
    }

    public static <T> BaseResponse<T> error(String code, String msg, String description) {
        return new BaseResponse<>(code, msg, description);
    }
}
