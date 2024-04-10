package com.dzy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.model.dto.collect.CollectAlbumRequest;
import com.dzy.model.entity.ReCollectAlbum;

/**
 * @author DZY
 * @description 针对表【re_collect_album(收藏专辑关联表)】的数据库操作Service
 * @createDate 2024-04-10 12:02:35
 */
public interface ReCollectAlbumService extends IService<ReCollectAlbum> {

    /**
     * 收藏 | 取消收藏 专辑
     *
     * @param collectAlbumRequest
     * @return
     */
    Boolean doCollectAlbum(CollectAlbumRequest collectAlbumRequest);

}
