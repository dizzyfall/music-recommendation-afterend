package com.dzy.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * 歌曲表
 *
 * @TableName song
 */
@TableName(value = "song")
@Data
@Component
public class Song implements Serializable {

    /**
     * 歌曲id
     */
    @TableId(value = "song_id", type = IdType.AUTO)
    private Long id;

    /**
     * 歌曲歌手id列表
     */
    @TableField(value = "song_singer_list_id")
    private String singerListId;

    /**
     * 歌曲作词者id
     */
    @TableField(value = "song_writer_id")
    private Long writerId;

    /**
     * 歌曲作曲者id
     */
    @TableField(value = "song_composer_id")
    private Long composerId;

    /**
     * 歌曲专辑id,歌曲没有专辑默认为-1
     */
    @TableField(value = "song_album_id")
    private Long albumId;

    /**
     * 歌曲歌词id
     */
    @TableField(value = "song_lyric_id")
    private Long lyricId;

    /**
     * 歌曲封面保存路径,歌曲有专辑使用专辑图片作为歌曲封面否则使用歌手头像
     */
    @TableField(value = "song_image_path")
    private String imagePath;

    /**
     * 歌曲名
     */
    @TableField(value = "song_title")
    private String title;

    /**
     * 歌曲简介
     */
    @TableField(value = "song_description")
    private String description;

    /**
     * 歌曲语种
     */
    @TableField(value = "song_lang")
    private String lang;

    /**
     * 歌曲流派
     */
    @TableField(value = "song_genre")
    private String genre;

    /**
     * 歌曲收藏数量
     */
    @TableField(value = "song_collection_count")
    private Integer collectionCount;

    /**
     * 歌曲点赞数量
     */
    @TableField(value = "song_favour_count")
    private Integer favourCount;

    /**
     * 歌曲评论数量
     */
    @TableField(value = "song_comment_count")
    private Integer commentCount;

    /**
     * 歌曲发行时间
     */
    @TableField(value = "song_publish_time")
    private Date publishTime;

    /**
     * 歌曲创建时间
     */
    @TableField(value = "song_create_time")
    private Date createTime;

    /**
     * 歌曲更新时间
     */
    @TableField(value = "song_update_time")
    private Date updateTime;

    /**
     * 逻辑删除
     */
    @TableField(value = "song_is_delete")
    @TableLogic
    private Integer isDelete;

    private static final long serialVersionUID = -1924436440097193684L;
}