package com.dzy.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * 专辑表
 *
 * @TableName album
 */
@TableName(value = "album")
@Data
@Component
public class Album implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 专辑id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 歌手id
     */
    @TableField(value = "singer_id")
    private Long singerId;
    /**
     * 合作歌手id列表
     * json字符串
     */
    @TableField(value = "singer_id_list")
    private String singerIdList;
    /**
     * 专辑名称
     */
    @TableField(value = "title")
    private String title;
    /**
     * 专辑简介
     */
    @TableField(value = "description")
    private String description;
    /**
     * 专辑封面保存路径
     */
    @TableField(value = "image_path")
    private String imagePath;
    /**
     * 专辑类型
     */
    @TableField(value = "type")
    private String type;
    /**
     * 专辑歌曲数量
     */
    @TableField(value = "song_count")
    private Integer songCount;
    /**
     * 专辑收藏数量
     */
    @TableField(value = "collect_count")
    private Integer collectCount;
    /**
     * 专辑点赞数量
     */
    @TableField(value = "favour_count")
    private Integer favourCount;
    /**
     * 专辑评论数量
     */
    @TableField(value = "comment_count")
    private Integer commentCount;
    /**
     * 专辑发行日期
     */
    @TableField(value = "publish_time")
    private Date publishTime;
    /**
     * 专辑创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;
    /**
     * 专辑更新时间
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