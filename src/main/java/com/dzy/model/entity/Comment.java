package com.dzy.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户评论表
 *
 * @TableName comment
 */
@TableName(value = "comment")
@Data
@Component
public class Comment implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 评论id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;
    /**
     * 用户评论内容
     */
    @TableField(value = "content")
    private String content;
    /**
     * 用户评论点赞数量
     */
    @TableField(value = "favour_count")
    private Long favourCount;
    /**
     * 回复数
     */
    @TableField(value = "reply_count")
    private Long replyCount;
    /**
     * 用户评论发布时间
     */
    @TableField(value = "publish_time")
    private Date publishTime;
    /**
     * 用户评论创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;
    /**
     * 用户评论更新时间
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