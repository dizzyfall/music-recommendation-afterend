package com.dzy.model.vo.collect;

import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.entity.Collect;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 收藏的歌曲、专辑、歌单数量视图
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/10  14:24
 */
@Data
public class CollectCountVO implements Serializable {

    private static final long serialVersionUID = 8110366763810126876L;

    private Long song_count;

    private Long album_count;

    private Long songlist_count;

    public static CollectCountVO objToVO(Collect collect) {
        if (collect == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        CollectCountVO collectCountVO = new CollectCountVO();
        try {
            BeanUtils.copyProperties(collect, collectCountVO);
        } catch (BusinessException e) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "Bean复制属性错误");
        }
        return collectCountVO;
    }

}
