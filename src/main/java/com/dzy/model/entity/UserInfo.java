package com.dzy.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 *
 * @TableName user_info
 */
@TableName(value = "user_info")
@Data
@Component
public class UserInfo implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户图片id
     */
    @TableField(value = "image_id")
    private Long imageId;
    /**
     * 用户昵称
     */
    @TableField(value = "nickname")
    private String nickname;
    /**
     * 用户简介
     */
    @TableField(value = "description")
    private String description;
    /**
     * 用户账号
     */
    @TableField(value = "account")
    private String account;
    /**
     * 用户密码
     */
    @TableField(value = "password")
    private String password;
    /**
     * 用户性别 0:男 1:女 2:其他
     */
    @TableField(value = "sex")
    private Integer sex;
    /**
     * 用户所属地
     */
    @TableField(value = "region")
    private String region;
    /**
     * 用户电话号码
     */
    @TableField(value = "phone")
    private String phone;
    /**
     * 用户邮箱
     */
    @TableField(value = "email")
    private String email;
    /**
     * 用户地址
     */
    @TableField(value = "address")
    private String address;
    /**
     * 用户生日
     */
    @TableField(value = "birthday")
    private Date birthday;
    /**
     * 用户关注数量
     */
    @TableField(value = "attention_count")
    private Integer attentionCount;
    /**
     * 用户粉丝数量
     */
    @TableField(value = "fan_count")
    private Integer fanCount;
    /**
     * 用户好友数量
     */
    @TableField(value = "friend_count")
    private Integer friendCount;
    /**
     * 用户访客数量
     */
    @TableField(value = "visitor_count")
    private Integer visitorCount;
    /**
     * 用户创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;
    /**
     * 用户更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;
    /**
     * 逻辑删除
     */
    @TableField(value = "is_delete")
    @TableLogic
    private Integer isDelete;
}