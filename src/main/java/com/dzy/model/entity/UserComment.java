package com.dzy.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户评论表
 *
 * @TableName user_comment
 */
@TableName(value = "user_comment")
@Data
@Component
public class UserComment implements Serializable {

    /**
     * 用户评论id
     */
    @TableId(value = "user_cmt_id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "user_cmt_user_id")
    private Long userId;

    /**
     * 用户评论内容
     */
    @TableField(value = "user_cmt_content")
    private String content;

    /**
     * 用户评论点赞数量
     */
    @TableField(value = "user_cmt_favour_count")
    private Long favourCount;

    /**
     * 用户评论发布时间
     */
    @TableField(value = "user_cmt_publish_time")
    private Date publishTime;

    /**
     * 用户评论创建时间
     */
    @TableField(value = "user_cmt_create_time")
    private Date createTime;

    /**
     * 用户评论更新时间
     */
    @TableField(value = "user_cmt_update_time")
    private Date updateTime;

    /**
     * 逻辑删除
     */
    @TableField(value = "user_cmt_is_delete")
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}