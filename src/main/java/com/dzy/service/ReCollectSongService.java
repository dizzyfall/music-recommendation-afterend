package com.dzy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.model.dto.collect.CollectSongRequest;
import com.dzy.model.entity.ReCollectSong;

/**
 * @author DZY
 * @description 针对表【re_collect_song(收藏歌曲关联表)】的数据库操作Service
 * @createDate 2024-04-09 23:21:12
 */
public interface ReCollectSongService extends IService<ReCollectSong> {

    /**
     * 收藏 | 取消收藏 歌曲
     *
     * @param collectSongRequest
     * @return
     */
    Boolean doCollectSong(CollectSongRequest collectSongRequest);

}
