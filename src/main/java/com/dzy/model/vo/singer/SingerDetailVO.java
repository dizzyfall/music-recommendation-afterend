package com.dzy.model.vo.singer;

import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.entity.Singer;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 歌手视图
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/10  11:49
 */
@Data
public class SingerDetailVO implements Serializable {

    private static final long serialVersionUID = 7255854788276766763L;

    /**
     * 歌手id
     */
    private Long singerId;

    /**
     * 歌手头像路径
     */
    private String avatarPath;

    /**
     * 歌手名字
     */

    private String singerName;

    /**
     * 歌手别名
     */
    private String alias;

    /**
     * 歌手拼音
     */
    private String spell;

    /**
     * 用户介绍
     */
    private String description;

    /**
     * 歌手地区
     */
    private Integer area;

    /**
     * 歌手类别
     */
    private Integer genre;


    /**
     * 歌手歌曲数量
     */
    private Integer songCount;

    /**
     * 歌手专辑数量
     */
    private Integer albumCount;

    /**
     * 歌手粉丝数量
     */
    private Integer fanCount;


    /**
     * Singer转SingerDetailVO
     * SingerDetailVO的属性只含有Song原本的属性
     *
     * @param singer
     * @return com.dzy.model.vo.singer.SingerDetailVO
     * @date 2024/4/30  11:54
     */
    public static SingerDetailVO objToVO(Singer singer) {
        if (singer == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        SingerDetailVO singerDetailVO = new SingerDetailVO();
        try {
            BeanUtils.copyProperties(singer, singerDetailVO);
        } catch (BusinessException e) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "Bean复制属性错误");
        }
        return singerDetailVO;
    }

}
