package com.dzy.model.job.easyexcel;

import java.util.Date;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/5/22  21:44
 */
public class AlbumExcel {

    private String singerName;
    /**
     * 专辑名称
     */
    private String title;
    /**
     * 专辑简介
     */
    private String description;
    /**
     * 专辑封面保存路径
     */
    private String imagePath;
    /**
     * 专辑歌曲数量
     */
    private Integer songCount;
    /**
     * 专辑收藏数量
     */
    private Integer collectCount;
    /**
     * 专辑点赞数量
     */
    private Integer favourCount;
    /**
     * 专辑评论数量
     */
    private Integer commentCount;
    /**
     * 专辑发行日期
     */
    private Date publishTime;
}
