package com.dzy.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户权限表
 *
 * @TableName user_authority
 */
@TableName(value = "user_authority")
@Data
@Component
public class UserAuthority implements Serializable {

    /**
     * 用户权限id
     */
    @TableId(value = "authority_user_id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户权限是否为管理员 0:是 1:不是
     */
    @TableField(value = "authority_is_admin")
    private Integer isAdmin;

    /**
     * 用户权限是否展示主页 0:展示 1:不展示
     */
    @TableField(value = "authority_show_home")
    private Integer showHome;

    /**
     * 用户权限是否展示收藏 0:展示 1:不展示
     */
    @TableField(value = "authority_show_collection")
    private Integer showCollection;

    /**
     * 用户权限是否展示歌单 0:展示 1:不展示
     */
    @TableField(value = "authority_show_songlist")
    private Integer showSonglist;

    /**
     * 用户权限是否展示归属地 0:展示 1:不展示
     */
    @TableField(value = "authority_show_region")
    private Integer showRegion;

    /**
     * 用户权限是否展示性别 0:展示 1:不展示
     */
    @TableField(value = "authority_show_sex")
    private Integer showSex;

    /**
     * 用户权限是否展示评论 0:展示 1:不展示
     */
    @TableField(value = "authority_show_comment")
    private Integer showComment;

    /**
     * 用户权限创建时间
     */
    @TableField(value = "authority_create_time")
    private Date createTime;

    /**
     * 用户权限更新时间
     */
    @TableField(value = "authority_update_time")
    private Date updateTime;

    /**
     * 逻辑删除
     */
    @TableField(value = "authority_is_delete")
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}