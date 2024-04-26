package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.ReCollectSonglistMapper;
import com.dzy.model.dto.collect.CollectSonglistRequest;
import com.dzy.model.entity.Collect;
import com.dzy.model.entity.ReCollectSonglist;
import com.dzy.model.entity.Songlist;
import com.dzy.service.CollectService;
import com.dzy.service.ReCollectSonglistService;
import com.dzy.service.SonglistService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author DZY
 * @description 针对表【re_collect_songlist(收藏歌单关联表)】的数据库操作Service实现
 * @createDate 2024-04-14 09:30:23
 */
@Service
public class ReCollectSonglistServiceImpl extends ServiceImpl<ReCollectSonglistMapper, ReCollectSonglist>
        implements ReCollectSonglistService {

    @Resource
    private CollectService collectService;

    @Resource
    private SonglistService songlistService;

    /**
     * 收藏 | 取消收藏 歌单
     *
     * @param collectSonglistRequest
     * @return java.lang.Boolean
     * @date 2024/4/14  9:35
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean doCollectSonglist(CollectSonglistRequest collectSonglistRequest) {
        if (collectSonglistRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long userId = collectSonglistRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long songlistId = collectSonglistRequest.getSonglistId();
        if (songlistId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //歌单是否存在
        Songlist songlist = songlistService.getById(songlistId);
        if (songlist == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "歌单不存在");
        }
        //是否已收藏
        QueryWrapper<ReCollectSonglist> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("songlist_id", songlistId);
        ReCollectSonglist oldCollectSonglist = this.getOne(queryWrapper);
        if (oldCollectSonglist != null) {
            boolean remove = this.remove(queryWrapper);
            if (remove) {
                //更新收藏表数据
                UpdateWrapper<Collect> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("user_id", userId).setSql("songlist_count = songlist_count - 1");
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
            ReCollectSonglist reCollectSonglist = new ReCollectSonglist();
            reCollectSonglist.setUserId(userId);
            reCollectSonglist.setSonglistId(songlistId);
            boolean isReCollectSonglistSave = this.save(reCollectSonglist);
            if (isReCollectSonglistSave) {
                //更新收藏表数据
                UpdateWrapper<Collect> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("user_id", userId).setSql("songlist_count = songlist_count + 1");
                boolean isCollectUpdate = collectService.update(updateWrapper);
                if (!isCollectUpdate) {
                    throw new BusinessException(StatusCode.UPDATE_ERROR, "收藏成功");
                }
            } else {
                throw new BusinessException(StatusCode.SYSTEM_ERROR);
            }
        }
        return true;
    }

}




