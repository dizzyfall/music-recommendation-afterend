package com.dzy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.model.dto.songlist.*;
import com.dzy.model.entity.Songlist;

/**
 * @author DZY
 * @description 针对表【songlist(歌单表)】的数据库操作Service
 * @createDate 2024-04-11 19:40:21
 */
public interface SonglistService extends IService<Songlist> {

    /**
     * 创建歌单
     *
     * @param songlistCreateRequest
     * @return
     */
    Boolean createSonglist(SonglistCreateRequest songlistCreateRequest);

    /**
     * 添加歌曲到歌单
     *
     * @param addSongRequest
     * @return
     */
    Boolean addSong(AddSongRequest addSongRequest);

    /**
     * 批量添加歌曲到歌单
     *
     * @param addBatchSongRequest
     * @return
     */
    Boolean addBatchSong(AddBatchSongRequest addBatchSongRequest);

    /**
     * 删除歌单
     *
     * @param songlistDeleteRequest
     * @return
     */
    Boolean deleteSonglist(SonglistDeleteRequest songlistDeleteRequest);

    /**
     * 删除歌单（批量删除）
     *
     * @param songlistDeleteBatchRequest
     * @return
     */
    Boolean deleteBatchSonglist(SonglistDeleteBatchRequest songlistDeleteBatchRequest);

    /**
     * 删除歌曲
     *
     * @param deleteSongRequest
     * @return
     */
    Boolean deleteSong(DeleteSongRequest deleteSongRequest);

    /**
     * 删除歌曲（批量删除）
     *
     * @param deleteBatchSongRequest
     * @return
     */
    Boolean deleteBatchSong(DeleteBatchSongRequest deleteBatchSongRequest);

}
