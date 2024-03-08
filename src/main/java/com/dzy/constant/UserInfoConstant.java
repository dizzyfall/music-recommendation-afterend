package com.dzy.constant;

/**
 * 用户常量
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/5  23:44
 */
public interface UserInfoConstant {
    /**
     * 用户登录态
     */
    String USER_LOGIN_STATE = "userLoginState";

    //权限
    /**
     * 普通用户
     */
    Integer DEFAULT_ROLE = 0;

    /**
     * 管理员
     */
    Integer ADMIN_ROLE = 1;

    String SALT = "pTfIsPSkhLS4";
}
