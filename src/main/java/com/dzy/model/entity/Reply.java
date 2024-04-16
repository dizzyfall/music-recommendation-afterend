package com.dzy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 回复表
 *
 * @TableName reply
 */
@TableName(value = "reply")
@Data
public class Reply implements Serializable {
    /**
     * 回复id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id(创建回复者id)
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 评论id
     */
    @TableField(value = "comment_id")
    private Long commentId;

    /**
     * 接收回复者id
     */
    @TableField(value = "receiver_id")
    private Long receiverId;

    /**
     * 评论类型
     */
    @TableField(value = "comment_type")
    private String commentType;

    /**
     * 回复内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 发布时间
     */
    @TableField(value = "publish_time")
    private Date publishTime;

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