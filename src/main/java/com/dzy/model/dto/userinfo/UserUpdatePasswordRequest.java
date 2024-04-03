package com.dzy.model.dto.userinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新请求参数封装
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/8  15:53
 */
@Data
public class UserUpdatePasswordRequest implements Serializable {

    private static final long serialVersionUID = -5124886432765364356L;

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户确认密码
     */
    private String checkPassword;
}
