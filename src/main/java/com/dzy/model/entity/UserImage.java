package com.dzy.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 用户图片表
 * @TableName user_image
 */
@TableName(value ="user_image")
@Data
@Component
public class UserImage implements Serializable {
    /**
     * 用户图片id
     */
    @TableId(value = "user_image_id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 用户头像保存路径
     */
    @TableField(value = "user_image_avatar_path")
    private String avatarPath;

    /**
     * 用户空间背景保存路径
     */
    @TableField(value = "user_image_background_path")
    private String backgroundPath;

    /**
     * 用户图片创建时间
     */
    @TableField(value = "user_image_create_time")
    private Date createTime;

    /**
     * 用户图片更新时间
     */
    @TableField(value = "user_image_update_time")
    private Date updateTime;

    /**
     * 逻辑删除
     */
    @TableField(value = "user_image_is_delete")
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}