package com.dzy.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * 歌手表
 *
 * @TableName singer
 */
@TableName(value = "singer")
@Data
@Component
public class Singer implements Serializable {

    /**
     * 歌手id
     */
    @TableId(value = "singer_id", type = IdType.AUTO)
    private Long id;

    /**
     * 歌手头像id
     */
    @TableField(value = "singer_avatar_path")
    private String avatarPath;

    /**
     * 歌手名字
     */
    @TableField(value = "singer_name")
    private String name;

    /**
     * 歌手别名
     */
    @TableField(value = "singer_alias")
    private String alias;

    /**
     * 歌手拼音
     */
    @TableField(value = "singer_spell")
    private String spell;

    /**
     * 用户介绍
     */
    @TableField(value = "singer_description")
    private String description;

    /**
     * 歌手地区
     */
    @TableField(value = "singer_area")
    private Integer area;

    /**
     * 歌手类别
     */
    @TableField(value = "singer_genre")
    private Integer genre;

    /**
     * 歌手性别
     */
    @TableField(value = "singer_sex")
    private Integer sex;

    /**
     * 歌手歌曲数量
     */
    @TableField(value = "singer_song_count")
    private Integer songCount;

    /**
     * 歌手专辑数量
     */
    @TableField(value = "singer_album_count")
    private Integer albumCount;

    /**
     * 歌手粉丝数量
     */
    @TableField(value = "singer_fan_count")
    private Integer fanCount;

    /**
     * 歌手创建时间
     */
    @TableField(value = "singer_create_time")
    private Date createTime;

    /**
     * 歌手更新时间
     */
    @TableField(value = "singer_update_time")
    private Date updateTime;

    /**
     * 逻辑删除 0:未删除 1:已删除
     */
    @TableField(value = "singer_is_delete")
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}