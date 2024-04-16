package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.CollectMapper;
import com.dzy.model.dto.collect.CollectQueryRequest;
import com.dzy.model.entity.*;
import com.dzy.model.vo.album.AlbumVO;
import com.dzy.model.vo.collect.CollectAlbumVO;
import com.dzy.model.vo.collect.CollectCountVO;
import com.dzy.model.vo.song.SongIntroVO;
import com.dzy.model.vo.songlist.SonglistIntroVO;
import com.dzy.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author DZY
 * @description 针对表【collect(用户收藏表)】的数据库操作Service实现
 * @createDate 2024-04-09 23:21:27
 */
@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect>
        implements CollectService {

    @Resource
    private ReCollectSongService reCollectSongService;

    @Resource
    private ReCollectAlbumService reCollectAlbumService;

    @Resource
    private ReCollectSonglistService reCollectSonglistService;

    @Resource
    private SongService songService;

    @Resource
    private AlbumService albumService;

    @Resource
    private SonglistService songlistService;

    @Resource
    private SingerService singerService;

    /**
     * 获取收藏的歌曲、专辑、歌单数量
     *
     * @param collectQueryRequest
     * @return
     */
    @Override
    public CollectCountVO getCollectCount(CollectQueryRequest collectQueryRequest) {
        if (collectQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long userId = collectQueryRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        Collect collect = this.getOne(queryWrapper);
        return CollectCountVO.objToVO(collect);
    }

    /**
     * 分页查询收藏的歌曲
     *
     * @param collectQueryRequest
     * @return
     */
    @Override
    public Page<SongIntroVO> listCollectSongByPage(CollectQueryRequest collectQueryRequest) {
        if (collectQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long userId = collectQueryRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        int pageCurrent = collectQueryRequest.getPageCurrent();
        int pageSize = collectQueryRequest.getPageSize();
        Page<ReCollectSong> page = new Page<>(pageCurrent, pageSize);
        QueryWrapper<ReCollectSong> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        Page<ReCollectSong> reCollectSongPage = reCollectSongService.page(page, queryWrapper);
        List<SongIntroVO> songIntroVOList = reCollectSongPage.getRecords().stream().map(reCollectSong -> {
            Long songId = reCollectSong.getSongId();
            return songService.getSongIntroById(songId);
        }).collect(Collectors.toList());
        Page<SongIntroVO> songIntroVOPage = new Page<>(pageCurrent, pageSize, reCollectSongPage.getTotal());
        return songIntroVOPage.setRecords(songIntroVOList);
    }

    /**
     * 分页查询收藏的专辑
     *
     * @param collectQueryRequest
     * @return
     */
    @Override
    public Page<CollectAlbumVO> listCollectAlbumByPage(CollectQueryRequest collectQueryRequest) {
        if (collectQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long userId = collectQueryRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        int pageCurrent = collectQueryRequest.getPageCurrent();
        int pageSize = collectQueryRequest.getPageSize();
        Page<ReCollectAlbum> page = new Page<>(pageCurrent, pageSize);
        QueryWrapper<ReCollectAlbum> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        Page<ReCollectAlbum> reCollectAlbumPage = reCollectAlbumService.page(page, queryWrapper);
        List<CollectAlbumVO> collectAlbumVOList = reCollectAlbumPage.getRecords().stream().map(reCollectAlbum -> {
            Long albumId = reCollectAlbum.getAlbumId();
            return getCollectAlbumVOById(albumId);
        }).collect(Collectors.toList());
        Page<CollectAlbumVO> collectAlbumVOListPage = new Page<>(pageCurrent, pageSize, reCollectAlbumPage.getTotal());
        return collectAlbumVOListPage.setRecords(collectAlbumVOList);
    }

    /**
     * 分页查询收藏的歌单
     *
     * @param collectQueryRequest
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.dzy.model.vo.songlist.SonglistIntroVO>
     * @date 2024/4/14  10:35
     */
    @Override
    public Page<SonglistIntroVO> listCollectSonglistByPage(CollectQueryRequest collectQueryRequest) {
        if (collectQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long userId = collectQueryRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        int pageCurrent = collectQueryRequest.getPageCurrent();
        int pageSize = collectQueryRequest.getPageSize();
        Page<ReCollectSonglist> page = new Page<>(pageCurrent, pageSize);
        QueryWrapper<ReCollectSonglist> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        Page<ReCollectSonglist> reCollectSonglistPage = reCollectSonglistService.page(page, queryWrapper);
        List<SonglistIntroVO> songlistIntroVOList = reCollectSonglistPage.getRecords().stream().map(reCollectSonglist -> {
            Long songlistId = reCollectSonglist.getSonglistId();
            return songlistService.getSonglistIntroVOById(songlistId);
        }).collect(Collectors.toList());
        Page<SonglistIntroVO> songlistIntroVOPage = new Page<>(pageCurrent, pageSize, reCollectSonglistPage.getTotal());
        return songlistIntroVOPage.setRecords(songlistIntroVOList);
    }

    //todo 几个视图解释清楚

    /**
     * 获取收藏的专辑简介视图
     *
     * @param albumId
     * @return
     */
    public CollectAlbumVO getCollectAlbumVOById(Long albumId) {
        if (albumId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Album album = albumService.getById(albumId);
        if (album == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "无此专辑");
        }
        CollectAlbumVO collectAlbumVO = new CollectAlbumVO();
        //todo 下面方法写到VO里
        AlbumVO albumVO = AlbumVO.objToVO(album);
        List<String> singerNameList = singerService.getSingerNameList(album.getSingerIdList());
        collectAlbumVO.setAlbumVO(albumVO);
        collectAlbumVO.setSingerNameList(singerNameList);
        return collectAlbumVO;
    }

}




