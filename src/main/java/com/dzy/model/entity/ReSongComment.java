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
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 歌曲id
     */
    @TableField(value = "song_id")
    private Long songId;

    /**
     * 评论id
     */
    @TableField(value = "comment_id")
    private Long commentId;

    /**
     * 评论用户id
     */
    @TableField(value = "create_user_id")
    private Long createUserId;

    /**
     * 接受回复用户id
     */
    @TableField(value = "receiver_id")
    private Long receiverId;

    /**
     * 回复用户id
     */
    @TableField(value = "replier_id")
    private Long replierId;

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