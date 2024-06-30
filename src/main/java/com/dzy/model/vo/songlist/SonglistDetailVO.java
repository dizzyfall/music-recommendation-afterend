package com.dzy.model.vo.songlist;

import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.entity.Songlist;
import com.dzy.model.vo.song.SongIntroVO;
import com.dzy.model.vo.userinfo.UserInfoIntroVO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/11  21:55
 */
@Data
public class SonglistDetailVO implements Serializable {

    private static final long serialVersionUID = 1599513186482336319L;

    /**
     * 歌单Id
     */
    private Long songlistId;

    /**
     * 歌单封面保存路径
     */
    private String imagePath;

    /**
     * 歌单名称
     */
    private String title;

    /**
     * 歌单简介
     */
    private String description;

    /**
     * 歌单歌曲数量
     */
    private Integer songCount;

    /**
     * 歌单收藏数量
     */
    private Integer collectCount;

    /**
     * 歌单点赞数量
     */
    private Integer favourCount;

    /**
     * 歌单评论数量
     */
    private Integer commentCount;

    /**
     * 歌单播放量
     */
    private Long playCount;

    /**
     * 歌单发行时间
     */
    private Date publishTime;

    /**
     * 歌单歌曲信息
     */
    private List<SongIntroVO> songIntroVOList;

    /**
     * 创建者信息简介
     */
    private UserInfoIntroVO creatorInfoIntroVO;

    /**
     * Songlist转SonglistDetailVO
     * SonglistDetailVO的属性只含有Songlist原本的属性
     *
     * @param songlist
     * @return com.dzy.model.vo.songlist.SonglistIntroVO
     * @date 2024/4/14  11:34
     */
    public static SonglistDetailVO objToVO(Songlist songlist) {
        if (songlist == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        SonglistDetailVO songlistDetailVO = new SonglistDetailVO();
        try {
            BeanUtils.copyProperties(songlist, songlistDetailVO);
        } catch (BusinessException e) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "Bean复制属性错误");
        }
        return songlistDetailVO;
    }

}
