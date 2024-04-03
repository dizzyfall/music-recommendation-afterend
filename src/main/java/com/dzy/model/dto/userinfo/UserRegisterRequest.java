package com.dzy.model.dto.userinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求参数封装
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/8  15:53
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -7348013305855075020L;

    /**
     * 用户账号
     */
    private String account;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户确认密码
     */
    private String checkPassword;
}
