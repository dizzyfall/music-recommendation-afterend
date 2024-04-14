package com.dzy.model.dto.songlist;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/13  21:03
 */
@Data
public class SonglistCommentCreateRequest implements Serializable {

    private static final long serialVersionUID = 8381327930317998990L;

    /**
     * 创建评论用户id
     */
    private Long userId;

    /**
     * 歌单id
     */
    private Long songlistId;

    /**
     * 用户评论内容
     */
    private String content;

}
