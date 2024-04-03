package com.dzy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论点赞关联表(硬删除)
 *
 * @TableName re_comment_favour
 */
@TableName(value = "re_comment_favour")
@Data
@Component
public class ReCommentFavour implements Serializable {
    /**
     *
     */
    @TableId(value = "re_comment_favour_id", type = IdType.AUTO)
    private Long reCommentFavourId;

    /**
     * 评论id
     */
    @TableField(value = "cmt_id")
    private Long cmtId;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     *
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     *
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}