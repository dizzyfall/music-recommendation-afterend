package com.dzy.model.dto.album;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/8  22:38
 */
@Data
public class AlbumCommentCreateRequest implements Serializable {

    private static final long serialVersionUID = -6319949768442554881L;

    /**
     * 创建评论用户id
     */
    private Long userId;

    /**
     * 专辑id
     */
    private Long albumId;

    /**
     * 用户评论内容
     */
    private String content;

}
