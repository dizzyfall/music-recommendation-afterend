package com.dzy.model.dto.comment;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/3  12:49
 */
@Data
public class ReplyCreateRequest implements Serializable {

    private static final long serialVersionUID = 8517205018693856605L;

    /**
     * 歌曲id
     */
    private Long songId;

    /**
     * 评论id
     */
    private Long commentId;

    /**
     * 创建评论用户id
     */
    private Long createUserId;

    /**
     * 用户评论内容
     */
    private String content;

    /**
     * 接受回复用户id
     */
    private Long receiverId;

    /**
     * 回复用户id
     */
    private Long replierId;

}
