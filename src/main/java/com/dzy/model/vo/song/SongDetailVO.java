package com.dzy.model.vo.song;

import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.entity.Song;
import com.dzy.utils.JsonUtil;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 歌曲详情视图
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/3  12:21
 */
@Data
public class SongDetailVO implements Serializable {

    private static final long serialVersionUID = -1039821772081244465L;

    /**
     * 歌曲id
     */
    private Long id;

    /**
     * 歌曲歌手id列表
     */
    private List<String> singerListId;

    /**
     * 歌曲歌手姓名列表
     * json字符串
     */
    private List<String> singerNameList;

    /**
     * 歌曲歌手姓名字符串（前端展示）
     */
    private String singerNameStr;

    /**
     * 歌曲作词者id
     */
    private Long writerId;

    /**
     * 歌曲作曲者id
     */
    private Long composerId;

    /**
     * 歌曲专辑id,歌曲没有专辑默认为-1
     */
    private Long albumId;

    /**
     * 歌曲歌词id
     */
    private Long lyricId;

    /**
     * 歌曲封面保存路径,歌曲有专辑使用专辑图片作为歌曲封面否则使用歌手头像
     */
    private String imagePath;

    /**
     * 歌曲名
     */
    private String title;

    /**
     * 歌曲简介
     */
    private String description;

    /**
     * 歌曲语种
     */
    private String lang;

    /**
     * 歌曲流派
     */
    private String genre;

    /**
     * 歌曲收藏数量
     */
    private Integer collectCount;

    /**
     * 歌曲点赞数量
     */
    private Integer favourCount;

    /**
     * 歌曲评论数量
     */
    private Integer commentCount;

    /**
     * 歌曲发行时间
     */
    private Date publishTime;

    /**
     * Song转SongDetailVO
     * SongDetailVO的属性只含有Song原本的属性
     *
     * @param song
     * @return com.dzy.model.vo.song.SongDetailVO
     * @date 2024/4/14  11:31
     */
    public static SongDetailVO objToVO(Song song) {
        if (song == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        SongDetailVO songDetailVO = new SongDetailVO();
        try {
            BeanUtils.copyProperties(song, songDetailVO);
            //歌手id列表json字符串转List集合对象
            songDetailVO.setSingerListId(JsonUtil.convertJsonToList(song.getSingerListId()));
        } catch (BusinessException e) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "Bean复制属性错误");
        }
        return songDetailVO;
    }

}
