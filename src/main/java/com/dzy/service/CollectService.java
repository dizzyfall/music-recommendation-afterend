package com.dzy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.model.dto.collect.CollectQueryRequest;
import com.dzy.model.entity.Collect;
import com.dzy.model.vo.collect.CollectAlbumVO;
import com.dzy.model.vo.collect.CollectCountVO;
import com.dzy.model.vo.song.SongIntroVO;
import com.dzy.model.vo.songlist.SonglistIntroVO;

/**
 * @author DZY
 * @description 针对表【collect(用户收藏表)】的数据库操作Service
 * @createDate 2024-04-09 23:21:27
 */
public interface CollectService extends IService<Collect> {

    /**
     * 获取收藏的歌曲、专辑、歌单数量
     *
     * @param collectQueryRequest
     * @return
     */
    CollectCountVO getCollectCount(CollectQueryRequest collectQueryRequest);

    /**
     * 分页查询收藏的歌曲
     *
     * @param collectQueryRequest
     * @return
     */
    Page<SongIntroVO> listCollectSongByPage(CollectQueryRequest collectQueryRequest);

    /**
     * 分页查询收藏的专辑
     *
     * @param collectQueryRequest
     * @return
     */
    Page<CollectAlbumVO> listCollectAlbumByPage(CollectQueryRequest collectQueryRequest);

    /**
     * 分页查询收藏的歌单
     *
     * @param collectQueryRequest
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.dzy.model.vo.songlist.SonglistIntroVO>
     * @date 2024/4/14  10:35
     */
    Page<SonglistIntroVO> listCollectSonglistByPage(CollectQueryRequest collectQueryRequest);

}
