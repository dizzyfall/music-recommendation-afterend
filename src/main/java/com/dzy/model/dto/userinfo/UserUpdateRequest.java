package com.dzy.model.dto.userinfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户更新请求参数封装
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/8  15:53
 */
@Data
public class UserUpdateRequest implements Serializable {

    private static final long serialVersionUID = -4066119876412280417L;

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户头像id
     */
    private Long avatarId;

    /**
     * 用户背景id
     */
    private Long backgroundId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户简介
     */
    private String description;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户确认密码
     */
    private String checkPassword;

    /**
     * 用户性别 0:男 1:女 2:其他
     */
    private Integer sex;

    /**
     * 用户所属地
     */
    private String region;

    /**
     * 用户电话号码
     */
    private String phone;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户地址
     */
    private String address;

    /**
     * 用户生日
     */
    private Date birthday;
}
