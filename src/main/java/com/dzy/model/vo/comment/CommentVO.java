package com.dzy.model.vo.comment;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/5  10:09
 */
@Data
public class CommentVO implements Serializable {

    private static final long serialVersionUID = 7939516196351738858L;

    /**
     * 歌曲id
     */
    private Long songId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户图片id
     */
    private Long image_id;


    /**
     * 用户头像保存路径
     */
    private String avatarPath;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户性别 0:男 1:女 2:其他
     */
    private Integer sex;

    /**
     * 用户所属地
     */
    private String region;

    /**
     * 用户评论内容
     */
    private String content;

    /**
     * 用户评论点赞数量
     */
    private Long favourCount;

    /**
     * 评论回复数量
     */
    private Long replyCount;

    /**
     * 用户评论发布时间
     */
    private Date publishTime;

}
