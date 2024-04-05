package com.dzy.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户图片表
 *
 * @TableName user_image
 */
@TableName(value = "user_image")
@Data
@Component
public class UserImage implements Serializable {
    /**
     * 用户图片id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 用户头像保存路径
     */
    @TableField(value = "avatar_path")
    private String avatarPath;

    /**
     * 用户空间背景保存路径
     */
    @TableField(value = "background_path")
    private String backgroundPath;

    /**
     * 用户图片创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 用户图片更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 逻辑删除
     */
    @TableField(value = "is_delete")
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}