package com.dzy.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * 歌曲回复关联表
 *
 * @TableName re_song_reply
 */
@TableName(value = "re_song_reply")
@Data
@Component
public class ReSongReply implements Serializable {
    /**
     * 关联表id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id(冗余字段)
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 歌曲id
     */
    @TableField(value = "song_id")
    private Long songId;

    /**
     * 评论id(冗余字段)
     */
    @TableField(value = "comment_id")
    private Long commentId;

    /**
     * 回复id
     */
    @TableField(value = "reply_id")
    private Long replyId;

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
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}