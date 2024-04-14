package com.dzy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.model.dto.collect.CollectSonglistRequest;
import com.dzy.model.entity.ReCollectSonglist;

/**
 * @author DZY
 * @description 针对表【re_collect_songlist(收藏歌单关联表)】的数据库操作Service
 * @createDate 2024-04-14 09:30:23
 */
public interface ReCollectSonglistService extends IService<ReCollectSonglist> {

    /**
     * 收藏 | 取消收藏 歌单
     *
     * @param collectSonglistRequest
     * @return java.lang.Boolean
     * @date 2024/4/14  9:35
     */
    Boolean doCollectSonglist(CollectSonglistRequest collectSonglistRequest);

}
