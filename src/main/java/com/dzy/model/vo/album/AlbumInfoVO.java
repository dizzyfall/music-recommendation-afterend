package com.dzy.model.vo.album;

import com.dzy.model.vo.song.SongIntroVO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 专辑详情视图
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/7  14:27
 */
@Data
public class AlbumInfoVO implements Serializable {

    private static final long serialVersionUID = -8209068249296875660L;

    /**
     * 专辑id
     */
    private Long id;

    /**
     * 专辑歌手id列表
     * json字符串
     */
    private String singerIdList;

    /**
     * 歌手姓名列表
     */
    private List<String> singerNameList;

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

    /**
     * 专辑歌曲信息
     */
    private SongIntroVO[] songVOList;
}
