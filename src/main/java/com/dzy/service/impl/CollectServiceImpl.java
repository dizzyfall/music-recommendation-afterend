package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.CollectMapper;
import com.dzy.model.dto.collect.CollectQueryRequest;
import com.dzy.model.entity.Collect;
import com.dzy.model.entity.ReCollectSong;
import com.dzy.model.vo.collect.CollectCountVO;
import com.dzy.model.vo.song.SongIntroVO;
import com.dzy.service.CollectService;
import com.dzy.service.ReCollectSongService;
import com.dzy.service.SongService;
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
    private SongService songService;

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
        Page<SongIntroVO> songIntroVOPage = new Page<>(pageCurrent, pageSize, page.getTotal());
        return songIntroVOPage.setRecords(songIntroVOList);
    }

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

}




