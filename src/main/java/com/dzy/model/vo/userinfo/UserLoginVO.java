package com.dzy.model.vo.userinfo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户返回登录信息视图（脱敏）
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/5  23:49
 */
@Data
public class UserLoginVO implements Serializable {

    private static final long serialVersionUID = -6338811984591369301L;

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户图片id
     */
    private Long imageId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户简介
     */
    private String description;

    /**
     * 用户关注数量
     */
    private Integer attentionCount;

    /**
     * 用户粉丝数量
     */
    private Integer fanCount;

    /**
     * 用户好友数量
     */
    private Integer friendCount;

    /**
     * 用户访客数量
     */
    private Integer visitorCount;

    /**
     * 用户创建时间
     */
    private Date createTime;

    /**
     * 用户更新时间
     */
    private Date updateTime;
}
