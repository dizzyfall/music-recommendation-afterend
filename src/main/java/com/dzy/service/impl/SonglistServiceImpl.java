package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.SonglistMapper;
import com.dzy.model.dto.songlist.AddSongBatchesRequest;
import com.dzy.model.dto.songlist.AddSongRequest;
import com.dzy.model.dto.songlist.SonglistCreateRequest;
import com.dzy.model.entity.ReSonglistSong;
import com.dzy.model.entity.Songlist;
import com.dzy.service.ReSonglistSongService;
import com.dzy.service.SonglistService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author DZY
 * @description 针对表【songlist(歌单表)】的数据库操作Service实现
 * @createDate 2024-04-11 19:40:21
 */
@Service
public class SonglistServiceImpl extends ServiceImpl<SonglistMapper, Songlist>
        implements SonglistService {

    @Resource
    private ReSonglistSongService reSonglistSongService;

    /**
     * 创建歌单
     * 空歌单（没有歌曲）
     *
     * @param songlistCreateRequest
     * @return
     */
    @Override
    public Boolean createSonglist(SonglistCreateRequest songlistCreateRequest) {
        if (songlistCreateRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long userId = songlistCreateRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        String title = songlistCreateRequest.getTitle();
        if (StringUtils.isBlank(title)) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Songlist songlist = new Songlist();
        songlist.setCreatorId(userId);
        songlist.setTitle(title);
        songlist.setPublishTime(new Date());
        boolean isSonglistSave = this.save(songlist);
        if (!isSonglistSave) {
            throw new BusinessException(StatusCode.CREATE_ERROR);
        }
        return isSonglistSave;
    }

    /**
     * 添加歌曲到歌单
     *
     * @param addSongRequest
     * @return
     */
    public Boolean addSong(AddSongRequest addSongRequest) {
        if (addSongRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long userId = addSongRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long songlistId = addSongRequest.getSonglistId();
        if (songlistId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long songId = addSongRequest.getSongId();
        if (songId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //添加到关联表
        ReSonglistSong reSonglistSong = new ReSonglistSong();
        reSonglistSong.setCreatorId(userId);
        reSonglistSong.setSonglistId(songlistId);
        reSonglistSong.setSongId(songId);
        boolean isReSonglistSongSave = reSonglistSongService.save(reSonglistSong);
        if (!isReSonglistSongSave) {
            throw new BusinessException(StatusCode.CREATE_ERROR);
        }
        //更新歌单表
        UpdateWrapper<Songlist> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("creator_id", userId).eq("id", songlistId).setSql("song_count = song_count + 1");
        boolean isSonglistUpdate = this.update(updateWrapper);
        if (!isSonglistUpdate) {
            throw new BusinessException(StatusCode.CREATE_ERROR);
        }
        return true;
    }

    /**
     * 批量添加歌曲到歌单
     *
     * @param addSongBatchesRequest
     * @return
     */
    @Override
    public Boolean addSongBatches(AddSongBatchesRequest addSongBatchesRequest) {
        if (addSongBatchesRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long userId = addSongBatchesRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long songlistId = addSongBatchesRequest.getSonglistId();
        if (songlistId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        List<Long> songIdList = addSongBatchesRequest.getSongIdList();
        if (CollectionUtils.isEmpty(songIdList)) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //添加到关联表
        List<ReSonglistSong> reSonglistSongList = new ArrayList<>();
        for (Long songId : songIdList) {
            ReSonglistSong reSonglistSong = new ReSonglistSong();
            reSonglistSong.setCreatorId(userId);
            reSonglistSong.setSonglistId(songlistId);
            reSonglistSong.setSongId(songId);
            reSonglistSongList.add(reSonglistSong);
        }
        boolean isReSonglistSongSave = reSonglistSongService.saveBatch(reSonglistSongList);
        if (!isReSonglistSongSave) {
            throw new BusinessException(StatusCode.CREATE_ERROR);
        }
        //更新歌单表
        UpdateWrapper<Songlist> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("creator_id", userId).eq("id", songlistId).setSql("song_count = song_count " + reSonglistSongList.size());
        boolean isSonglistUpdate = this.update(updateWrapper);
        if (!isSonglistUpdate) {
            throw new BusinessException(StatusCode.CREATE_ERROR);
        }
        return true;
    }

}




