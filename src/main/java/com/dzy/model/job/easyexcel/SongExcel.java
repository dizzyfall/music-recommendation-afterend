package com.dzy.model.job.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/5/22  21:44
 */
@Data
public class SongExcel {
//    /**
//     * 歌曲歌手id列表
//     */
//    private String singerListId;
//    /**
//     * 歌曲封面保存路径,歌曲有专辑使用专辑图片作为歌曲封面否则使用歌手头像
//     */
//    private String imagePath;
    /**
     * 歌曲名
     */
    @ExcelProperty("title")
    private String title;
//    /**
//     * 歌曲简介
//     */
//    private String description;
    /**
     * 歌曲播放数量
     */
    @ExcelProperty("playCount")
    private String playCount;
    /**
     * 歌曲发行时间
     */
    @ExcelProperty("publishTime")
    private String publishTime;
}
