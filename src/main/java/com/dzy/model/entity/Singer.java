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
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 歌手头像路径
     */
    @TableField(value = "avatar_path")
    private String avatarPath;

    /**
     * 歌手姓名
     */
    @TableField(value = "singer_name")
    private String singerName;

    /**
     * 歌手别名
     */
    @TableField(value = "alias")
    private String alias;

    /**
     * 歌手姓名拼音
     */
    @TableField(value = "spell")
    private String spell;

    /**
     * 歌手介绍
     */
    @TableField(value = "description")
    private String description;

    /**
     * 歌手地区
     */
    @TableField(value = "area")
    private Integer area;

    /**
     * 歌手类别
     */
    @TableField(value = "genre")
    private Integer genre;

    /**
     * 歌手性别
     */
    @TableField(value = "sex")
    private Integer sex;

    /**
     * 歌手歌曲数量
     */
    @TableField(value = "song_count")
    private Integer songCount;

    /**
     * 歌手专辑数量
     */
    @TableField(value = "album_count")
    private Integer albumCount;

    /**
     * 歌手粉丝数量
     */
    @TableField(value = "fan_count")
    private Integer fanCount;

    /**
     * 歌手创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 歌手更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 逻辑删除 0:未删除 1:已删除
     */
    @TableField(value = "is_delete")
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}