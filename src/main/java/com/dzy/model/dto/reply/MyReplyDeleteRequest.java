package com.dzy.model.dto.reply;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/7  11:58
 */
@Data
public class MyReplyDeleteRequest implements Serializable {

    private static final long serialVersionUID = -5240915927090613688L;

    private Long userId;

    private Long replyId;

}
