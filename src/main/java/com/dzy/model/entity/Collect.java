package com.dzy.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户收藏表
 *
 * @TableName collect
 */
@TableName(value = "collect")
@Data
@Component
public class Collect implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 用户收藏id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;
    /**
     * 用户收藏封面id
     */
    @TableField(value = "image_id")
    private Long imageId;
    /**
     * 用户收藏歌曲数量
     */
    @TableField(value = "song_count")
    private Integer songCount;
    /**
     * 用户收藏专辑数量
     */
    @TableField(value = "album_count")
    private Integer albumCount;
    /**
     * 用户收藏歌单数量
     */
    @TableField(value = "songlist_count")
    private Integer songlistCount;
    /**
     * 用户收藏创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;
    /**
     * 用户收藏更新时间
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