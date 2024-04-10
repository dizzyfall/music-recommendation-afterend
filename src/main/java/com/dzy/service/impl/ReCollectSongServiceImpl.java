package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.ReCollectSongMapper;
import com.dzy.model.dto.collect.CollectSongCreateRequest;
import com.dzy.model.entity.Collect;
import com.dzy.model.entity.ReCollectSong;
import com.dzy.service.CollectService;
import com.dzy.service.ReCollectSongService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author DZY
 * @description 针对表【re_collect_song(收藏歌曲关联表)】的数据库操作Service实现
 * @createDate 2024-04-09 23:21:12
 */
@Service
public class ReCollectSongServiceImpl extends ServiceImpl<ReCollectSongMapper, ReCollectSong>
        implements ReCollectSongService {

    @Resource
    private CollectService collectService;

    /**
     * 收藏歌曲
     *
     * @param collectSongCreateRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createCollectSong(CollectSongCreateRequest collectSongCreateRequest) {
        if (collectSongCreateRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long userId = collectSongCreateRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long songId = collectSongCreateRequest.getSongId();
        if (songId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //是否已收藏
        QueryWrapper<ReCollectSong> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("song_id", songId);
        ReCollectSong oldCollectSong = this.getOne(queryWrapper);
        //已收藏
        if (oldCollectSong != null) {
            //移除关联表信息
            boolean remove = this.remove(queryWrapper);
            if (remove) {
                //更新收藏表数据
                UpdateWrapper<Collect> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("user_id", userId).setSql("song_count=song_count-1");
                boolean isUpdate = collectService.update(updateWrapper);
                if (!isUpdate) {
                    throw new BusinessException(StatusCode.UPDATE_ERROR, "取消收藏失败");
                }
            } else {
                throw new BusinessException(StatusCode.SYSTEM_ERROR);
            }
        } else {
            //没有收藏
            //关联表添加数据
            ReCollectSong reCollectSong = new ReCollectSong();
            reCollectSong.setUserId(userId);
            reCollectSong.setSongId(songId);
            boolean save = this.save(reCollectSong);
            if (save) {
                //更新收藏表数据
                UpdateWrapper<Collect> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("user_id", userId).setSql("song_count=song_count+1");
                boolean isUpdate = collectService.update(updateWrapper);
                if (!isUpdate) {
                    throw new BusinessException(StatusCode.UPDATE_ERROR, "收藏成功");
                }
            } else {
                throw new BusinessException(StatusCode.SYSTEM_ERROR);
            }
        }
        return true;
    }

}




