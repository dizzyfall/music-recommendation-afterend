package com.dzy.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 歌曲表
 * @TableName song
 */
@TableName(value ="song")
@Data
@Component
public class Song implements Serializable {
    /**
     * 歌曲id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 歌曲歌手id列表
     */
    @TableField(value = "singer_list_id")
    private String singerListId;

    /**
     * 歌曲作词者id
     */
    @TableField(value = "writer_id")
    private Long writerId;

    /**
     * 歌曲作曲者id
     */
    @TableField(value = "composer_id")
    private Long composerId;

    /**
     * 歌曲专辑id,歌曲没有专辑默认为-1
     */
    @TableField(value = "album_id")
    private Long albumId;

    /**
     * 歌曲歌词id
     */
    @TableField(value = "lyric_id")
    private Long lyricId;

    /**
     * 歌曲封面保存路径,歌曲有专辑使用专辑图片作为歌曲封面否则使用歌手头像
     */
    @TableField(value = "image_path")
    private String imagePath;

    /**
     * 歌曲名
     */
    @TableField(value = "title")
    private String title;

    /**
     * 歌曲简介
     */
    @TableField(value = "description")
    private String description;

    /**
     * 歌曲语种
     */
    @TableField(value = "lang")
    private String lang;

    /**
     * 歌曲流派
     */
    @TableField(value = "genre")
    private String genre;

    /**
     * 歌曲收藏数量
     */
    @TableField(value = "collect_count")
    private Integer collectCount;

    /**
     * 歌曲点赞数量
     */
    @TableField(value = "favour_count")
    private Integer favourCount;

    /**
     * 歌曲评论数量
     */
    @TableField(value = "comment_count")
    private Integer commentCount;

    /**
     * 歌曲发行时间
     */
    @TableField(value = "publish_time")
    private Date publishTime;

    /**
     * 歌曲创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 歌曲更新时间
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