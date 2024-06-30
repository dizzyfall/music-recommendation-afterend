package com.dzy.model.job.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/5/22  21:42
 */
@Data
public class SingerExcel {
    /**
     * 歌手头像路径
     */
    @ExcelProperty(value = "avatar_path")
    private String avatarPath;

    /**
     * 歌手姓名
     */
    @ExcelProperty(value = "singer_name")
    private String singerName;

    /**
     * 歌手别名
     */
    @ExcelProperty(value = "alias")
    private String alias;

    /**
     * 歌手姓名拼音
     */
    @ExcelProperty(value = "spell")
    private String spell;

    /**
     * 歌手地区
     */
    @ExcelProperty(value = "area")
    private Integer area;

    /**
     * 歌手类别
     */
    @ExcelProperty(value = "genre")
    private Integer genre;

    /**
     * 歌手性别
     */
    @ExcelProperty(value = "sex")
    private Integer sex;

    /**
     * 歌手歌曲数量
     */
    @ExcelProperty(value = "song_count")
    private Integer songCount;

    /**
     * 歌手专辑数量
     */
    @ExcelProperty(value = "album_count")
    private Integer albumCount;

    /**
     * 歌手粉丝数量
     */
    @ExcelProperty(value = "fan_count")
    private Integer fanCount;

}
