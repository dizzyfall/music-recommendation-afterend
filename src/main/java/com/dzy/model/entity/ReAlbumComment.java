package com.dzy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 专辑评论关联表
 *
 * @TableName re_album_comment
 */
@TableName(value = "re_album_comment")
@Data
public class ReAlbumComment implements Serializable {
    /**
     * 专辑评论id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 专辑id
     */
    @TableField(value = "album_id")
    private Long albumId;

    /**
     * 评论id
     */
    @TableField(value = "comment_id")
    private Long commentId;

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
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}