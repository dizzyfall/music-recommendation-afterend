package com.dzy.model.dto.reply;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/3  12:49
 */
@Data
public class ReplyCreateRequest implements Serializable {

    private static final long serialVersionUID = -111253510228671022L;

    /**
     * 用户id(创建回复者id)
     */
    private Long userId;

    /**
     * 评论id
     */
    private Long commentId;

    /**
     * 接收回复者id
     */
    private Long receiverId;

    /**
     * 回复内容
     */
    private String content;

    /**
     * 评论类型
     */
    private String commentType;

}
