package com.dzy.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName re_songlist_song
 */
@TableName(value = "re_songlist_song")
@Data
@Component
public class ReSonglistSong implements Serializable {
    /**
     * 关联表id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建者id
     */
    @TableField(value = "creator_id")
    private Long creatorId;

    /**
     * 歌单id
     */
    @TableField(value = "songlist_id")
    private Long songlistId;

    /**
     * 歌曲id
     */
    @TableField(value = "song_id")
    private Long songId;

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