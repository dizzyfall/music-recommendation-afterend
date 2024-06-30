package com.dzy.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户评分表
 *
 * @TableName user_rating
 */
@TableName(value = "user_rating")
@Data
@Component
public class UserRating implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 用户评分id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 歌曲id
     */
    @TableField(value = "song_id")
    private Long songId;

    /**
     * 歌曲评分
     */
    @TableField(value = "song_rating")
    private Double songRating;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
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