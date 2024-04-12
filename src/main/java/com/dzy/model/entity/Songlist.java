package com.dzy.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * 歌单表
 *
 * @TableName songlist
 */
@TableName(value = "songlist")
@Data
@Component
public class Songlist implements Serializable {
    /**
     * 歌单id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 歌单创建者id
     */
    @TableField(value = "creator_id")
    private Long creatorId;

    /**
     * 歌单封面保存路径
     */
    @TableField(value = "image_path")
    private String imagePath;

    /**
     * 歌单名称
     */
    @TableField(value = "title")
    private String title;

    /**
     * 歌单简介
     */
    @TableField(value = "description")
    private String description;

    /**
     * 歌单歌曲数量
     */
    @TableField(value = "song_count")
    private Integer songCount;

    /**
     * 歌单收藏数量
     */
    @TableField(value = "collect_count")
    private Integer collectCount;

    /**
     * 歌单点赞数量
     */
    @TableField(value = "favour_count")
    private Integer favourCount;

    /**
     * 歌单评论数量
     */
    @TableField(value = "comment_count")
    private Integer commentCount;

    /**
     * 播放量
     */
    @TableField(value = "play_count")
    private Long playCount;

    /**
     * 歌单发行时间
     */
    @TableField(value = "publish_time")
    private Date publishTime;

    /**
     * 歌单创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 歌单更新时间
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