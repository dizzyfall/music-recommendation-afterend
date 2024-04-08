package com.dzy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.model.dto.album.AlbumQueryRequest;
import com.dzy.model.dto.album.AlbumSongQueryRequest;
import com.dzy.model.entity.Album;
import com.dzy.model.vo.album.AlbumInfoVO;
import com.dzy.model.vo.album.AlbumSongVO;
import com.dzy.model.vo.album.AlbumVO;

import java.util.List;

/**
 * @author DZY
 * @description 针对表【album(专辑表)】的数据库操作Service
 * @createDate 2024-04-07 13:26:21
 */
public interface AlbumService extends IService<Album> {

    /**
     * 分页查询歌手专辑
     *
     * @param albumQueryRequest
     * @return
     */
    Page<AlbumVO> listAlbumByPage(AlbumQueryRequest albumQueryRequest);

    /**
     * album转albumVO
     *
     * @param album
     * @return
     */
    AlbumVO albumToAlbumVO(Album album);

    /**
     * Album转AlbumInfoVO
     *
     * @param album
     * @return
     */
    AlbumInfoVO albumToAlbumInfoVO(Album album);

    /**
     * 查询指定专辑信息
     *
     * @param albumSongQueryRequest
     * @return
     */
    AlbumInfoVO searchAlbumInfo(AlbumSongQueryRequest albumSongQueryRequest);

    /**
     * 查询指定专辑所有歌曲
     *
     * @param albumSongQueryRequest
     * @return
     */
    List<AlbumSongVO> listSong(AlbumSongQueryRequest albumSongQueryRequest);

}
