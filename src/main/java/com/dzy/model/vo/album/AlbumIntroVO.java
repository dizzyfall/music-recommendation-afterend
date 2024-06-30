package com.dzy.model.vo.album;

import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.entity.Album;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 专辑简介视图
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/7  13:49
 */
@Data
public class AlbumIntroVO implements Serializable {

    private static final long serialVersionUID = -4559252108673377544L;

    /**
     * 专辑id
     */
    private Long albumId;

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

    /**
     * Album转AlbumVO
     *
     * @param album
     * @return
     */
    public static AlbumIntroVO objToVO(Album album) {
        if (album == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        AlbumIntroVO albumIntroVO = new AlbumIntroVO();
        try {
            BeanUtils.copyProperties(album, albumIntroVO);
        } catch (BusinessException e) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "Bean复制属性错误");
        }
        return albumIntroVO;
    }

}
