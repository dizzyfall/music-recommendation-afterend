package com.dzy.model.dto.recommend;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/5/21  13:57
 */
@Data
public class RecommendSongQueryRequest implements Serializable {

    private static final long serialVersionUID = -2374401798979546932L;

    private int n;

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
