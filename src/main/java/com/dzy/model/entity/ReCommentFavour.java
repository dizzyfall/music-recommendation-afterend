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

    private static final long serialVersionUID = -261520587293968985L;

    /**
     * 关联id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 评论id
     */
    @TableField(value = "comment_id")
    private Long commentId;
    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;
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
}