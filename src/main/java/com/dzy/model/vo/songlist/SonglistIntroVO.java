package com.dzy.model.vo.songlist;

import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.entity.Songlist;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/11  21:55
 */
@Data
public class SonglistIntroVO implements Serializable {

    private static final long serialVersionUID = -5233970639828760074L;

    /**
     * 歌单Id
     */
    private Long songlistId;

    /**
     * 歌单创建者姓名
     */
    private String creatorName;

    /**
     * 歌单封面保存路径
     */
    private String imagePath;

    /**
     * 歌单名称
     */
    private String title;

    /**
     * 歌单歌曲数量
     */
    private Integer songCount;

    /**
     * 播放量
     */
    private Long playCount;

    /**
     * 歌单发行时间
     */
    private Date publishTime;

    /**
     * Songlist转SonglistIntroVO
     * SonglistIntroVO的属性只含有Songlist原本的属性
     *
     * @param songlist
     * @return com.dzy.model.vo.songlist.SonglistIntroVO
     * @date 2024/4/14  11:34
     */
    public static SonglistIntroVO objToVO(Songlist songlist) {
        if (songlist == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        SonglistIntroVO songlistIntroVO = new SonglistIntroVO();
        try {
            BeanUtils.copyProperties(songlist, songlistIntroVO);
        } catch (BusinessException e) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "Bean复制属性错误");
        }
        return songlistIntroVO;
    }

}
