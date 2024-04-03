package com.dzy.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * 歌曲用户评论关联表
 *
 * @TableName re_song_comment
 */
@TableName(value = "re_song_comment")
@Data
@Component
public class ReSongComment implements Serializable {
    /**
     * 关联表id
     */
    @TableId(value = "re_song_comment_id", type = IdType.AUTO)
    private Long reSongCommentId;

    /**
     * 歌曲id
     */
    @TableField(value = "song_id")
    private Long songId;

    /**
     * 评论id
     */
    @TableField(value = "user_cmt_id")
    private Long userCmtId;

    /**
     * 评论用户id
     */
    @TableField(value = "create_user_id")
    private Long createUserId;

    /**
     * 被回复用户id
     */
    @TableField(value = "follower_id")
    private Long followerId;

    /**
     * 回复用户id
     */
    @TableField(value = "reply_user_id")
    private Long replyUserId;

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

    /**
     *
     */
    @TableField(value = "is_delete")
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}