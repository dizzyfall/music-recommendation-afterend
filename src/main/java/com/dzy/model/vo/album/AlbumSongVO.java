package com.dzy.model.vo.album;

import lombok.Data;

import java.util.List;

/**
 * 专辑中的歌曲简介视图
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/7  15:05
 */
@Data
public class AlbumSongVO {

    /**
     * 歌曲名
     */
    private String title;

    /**
     * 歌曲歌手姓名列表
     * json字符串
     */
    private List<String> singerNameList;

    //todo 歌曲时长字段

}
