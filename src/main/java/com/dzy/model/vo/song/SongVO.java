package com.dzy.model.vo.song;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/3  12:21
 */
@Data
public class SongVO implements Serializable {

    private static final long serialVersionUID = 5106304122881221758L;

    /**
     * 歌曲id
     */
    private Long songId;

    /**
     * 歌曲名
     */
    private String title;

    /**
     * 创建评论用户id
     */
    private Long createUserId;

    /**
     * 用户评论内容
     */
    private String content;

    /**
     * 用户评论点赞数量
     */
    private Long favourCount;

    /**
     * 用户评论发布时间
     */
    private Date publishTime;

    /**
     * 被回复用户id
     */
    private Long followerId;

    /**
     * 回复用户id
     */
    private Long replyUserId;
}
