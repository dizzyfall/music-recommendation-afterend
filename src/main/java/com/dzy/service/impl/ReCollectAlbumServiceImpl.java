package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.ReCollectAlbumMapper;
import com.dzy.model.dto.collect.CollectAlbumRequest;
import com.dzy.model.entity.Album;
import com.dzy.model.entity.Collect;
import com.dzy.model.entity.ReCollectAlbum;
import com.dzy.service.AlbumService;
import com.dzy.service.CollectService;
import com.dzy.service.ReCollectAlbumService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author DZY
 * @description 针对表【re_collect_album(收藏专辑关联表)】的数据库操作Service实现
 * @createDate 2024-04-10 12:02:35
 */
@Service
public class ReCollectAlbumServiceImpl extends ServiceImpl<ReCollectAlbumMapper, ReCollectAlbum>
        implements ReCollectAlbumService {

    @Resource
    private CollectService collectService;

    @Resource
    private AlbumService albumService;

    /**
     * 收藏 | 取消收藏 专辑
     *
     * @param collectAlbumRequest
     * @return
     */
    @Override
    public Boolean doCollectAlbum(CollectAlbumRequest collectAlbumRequest) {
        if (collectAlbumRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long userId = collectAlbumRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long albumId = collectAlbumRequest.getAlbumId();
        if (albumId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //专辑是否存在
        Album album = albumService.getById(albumId);
        if (album == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "专辑不存在");
        }
        //是否已收藏
        QueryWrapper<ReCollectAlbum> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("album_id", albumId);
        ReCollectAlbum oldCollectAlbum = this.getOne(queryWrapper);
        if (oldCollectAlbum != null) {
            boolean remove = this.remove(queryWrapper);
            if (remove) {
                //更新收藏表数据
                UpdateWrapper<Collect> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("user_id", userId).setSql("album_count = album_count - 1");
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
            ReCollectAlbum reCollectAlbum = new ReCollectAlbum();
            reCollectAlbum.setUserId(userId);
            reCollectAlbum.setAlbumId(albumId);
            boolean save = this.save(reCollectAlbum);
            if (save) {
                //更新收藏表数据
                UpdateWrapper<Collect> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("user_id", userId).setSql("album_count = album_count + 1");
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




