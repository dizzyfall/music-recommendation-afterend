package com.dzy.model.vo.song;

import lombok.Data;

import java.io.Serializable;

/**
 * 歌曲详情视图
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/3  12:21
 */
@Data
public class SongIntroVO implements Serializable {

    private static final long serialVersionUID = -1039821772081244465L;

    /**
     * 歌曲id
     */
    private Long songId;

    /**
     * 歌曲名
     */
    private String title;

    /**
     * 歌曲歌手姓名字符串（前端展示）
     */
    private String singerNameStr;

    /**
     * 歌曲评分
     */
    private Double rating;

}
