package com.dzy.model.dto.userrating;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/5/20  23:19
 */
@Data
public class SongRatingCreateRequest implements Serializable {

    private static final long serialVersionUID = -8915331070664442393L;

    private int n;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 歌曲id
     */
    private Long songId;

    /**
     * 歌曲评分
     */
    private Double songRating;

}
