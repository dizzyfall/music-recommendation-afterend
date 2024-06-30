package com.dzy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 回复点赞关联表(硬删除)
 *
 * @TableName re_reply_favour
 */
@TableName(value = "re_reply_favour")
@Data
public class ReReplyFavour implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 关联id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 回复id
     */
    @TableField(value = "reply_id")
    private Long replyId;
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