package com.dzy.model.dto.song;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/3  12:49
 */
@Data
public class SongCommentCreateRequest implements Serializable {

    private static final long serialVersionUID = 8517205018693856605L;

    /**
     * 创建评论用户id
     */
    private Long userId;

    /**
     * 歌曲id
     */
    private Long songId;

    /**
     * 用户评论内容
     */
    private String content;

}
