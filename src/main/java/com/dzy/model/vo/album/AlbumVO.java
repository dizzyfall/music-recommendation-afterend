package com.dzy.model.vo.album;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 专辑简介视图
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/7  13:49
 */
@Data
public class AlbumVO implements Serializable {

    private static final long serialVersionUID = -4559252108673377544L;

    /**
     * 专辑id
     */
    private Long id;

    /**
     * 专辑名称
     */
    private String title;

    /**
     * 专辑封面保存路径
     */
    private String imagePath;

    /**
     * 专辑歌曲数量
     */
    private Integer songCount;

    /**
     * 专辑发行日期
     */
    private Date publishTime;

}
