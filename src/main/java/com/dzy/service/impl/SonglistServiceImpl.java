package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.ReSonglistSongMapper;
import com.dzy.mapper.SonglistMapper;
import com.dzy.model.dto.songlist.*;
import com.dzy.model.entity.*;
import com.dzy.model.vo.comment.CommentVO;
import com.dzy.model.vo.songlist.SonglistIntroVO;
import com.dzy.service.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    private ReSonglistSongMapper reSonglistSongMapper;

    @Resource
    private SongService songService;

    @Resource
    private CommentService commentService;

    @Resource
    private ReSonglistCommentService reSonglistCommentService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private ReplyService replyService;

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
    @Override
    @Transactional(rollbackFor = Exception.class)
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
     * @param addBatchSongRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addBatchSong(AddBatchSongRequest addBatchSongRequest) {
        if (addBatchSongRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long userId = addBatchSongRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long songlistId = addBatchSongRequest.getSonglistId();
        if (songlistId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        List<Long> songIdList = addBatchSongRequest.getSongIdList();
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
        updateWrapper.eq("creator_id", userId).eq("id", songlistId).setSql("song_count = song_count + " + reSonglistSongList.size());
        boolean isSonglistUpdate = this.update(updateWrapper);
        if (!isSonglistUpdate) {
            throw new BusinessException(StatusCode.CREATE_ERROR);
        }
        return true;
    }

    /**
     * 删除歌单
     *
     * @param songlistDeleteRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteSonglist(SonglistDeleteRequest songlistDeleteRequest) {
        if (songlistDeleteRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long userId = songlistDeleteRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long songlistId = songlistDeleteRequest.getSonglistId();
        if (songlistId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //删除关联表数据
        QueryWrapper<ReSonglistSong> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("creator_id", userId).eq("songlist_id", songlistId);
        boolean isSongRemove = reSonglistSongService.remove(queryWrapper);
        if (!isSongRemove) {
            throw new BusinessException(StatusCode.DELETE_ERROR);
        }
        //删除歌单数据
        boolean isSonglistRemove = this.removeById(songlistId);
        if (!isSonglistRemove) {
            throw new BusinessException(StatusCode.DELETE_ERROR);
        }
        return true;
    }

    /**
     * 删除歌单（批量删除）
     *
     * @param songlistDeleteBatchRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteBatchSonglist(SonglistDeleteBatchRequest songlistDeleteBatchRequest) {
        if (songlistDeleteBatchRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long userId = songlistDeleteBatchRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        List<Long> songlistIdList = songlistDeleteBatchRequest.getSonglistIdList();
        if (CollectionUtils.isEmpty(songlistIdList)) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //删除关联表数据
        //Boolean isDelete = reSonglistSongMapper.deleteBatchBySonglistIds(userId, songlistIdList);
        QueryWrapper<ReSonglistSong> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("songlist_id", songlistIdList);
        List<Long> reSonglistSongIdList = reSonglistSongService.list(queryWrapper).stream().map(ReSonglistSong::getId).collect(Collectors.toList());
        boolean isDelete = this.removeBatchByIds(reSonglistSongIdList);
        if (!isDelete) {
            throw new BusinessException(StatusCode.DELETE_ERROR);
        }
        //删除歌单表数据
        boolean isSonglistRemove = this.removeBatchByIds(songlistIdList);
        if (!isSonglistRemove) {
            throw new BusinessException(StatusCode.DELETE_ERROR);
        }
        return true;
    }

    /**
     * 删除歌曲
     *
     * @param deleteSongRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteSong(DeleteSongRequest deleteSongRequest) {
        if (deleteSongRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long userId = deleteSongRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long songlistId = deleteSongRequest.getSonglistId();
        if (songlistId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long songId = deleteSongRequest.getSongId();
        if (songId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //歌曲是否存在
        Song song = songService.getById(songId);
        if (song == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "歌曲不存在");
        }
        //歌单是否存在
        Songlist songlist = this.getById(songlistId);
        if (songlist == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "歌单不存在");
        }
        //删除关联表数据
        QueryWrapper<ReSonglistSong> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("creator_id", userId).eq("songlist_id", songlistId).eq("song_id", songId);
        boolean isSongRemove = reSonglistSongService.remove(queryWrapper);
        if (!isSongRemove) {
            throw new BusinessException(StatusCode.DELETE_ERROR, "删除songlist-song关联表数据失败");
        }
        //更新歌单表数据
        UpdateWrapper<Songlist> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("creator_id", userId).eq("id", songlistId).setSql("song_count = song_count - 1");
        boolean isSonglistupdate = this.update(updateWrapper);
        if (!isSonglistupdate) {
            throw new BusinessException(StatusCode.UPDATE_ERROR, "更新歌单歌曲数量失败");
        }
        return true;
    }

    /**
     * 删除歌曲（批量删除）
     *
     * @param deleteBatchSongRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteBatchSong(DeleteBatchSongRequest deleteBatchSongRequest) {
        if (deleteBatchSongRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long userId = deleteBatchSongRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long songlistId = deleteBatchSongRequest.getSonglistId();
        if (songlistId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        List<Long> songIdList = deleteBatchSongRequest.getSongIdList();
        if (CollectionUtils.isEmpty(songIdList)) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //歌曲是否存在
        for (Long songId : songIdList) {
            Song song = songService.getById(songId);
            if (song == null) {
                throw new BusinessException(StatusCode.PARAMS_ERROR, "歌曲不存在");
            }
        }
        //歌单是否存在
        Songlist songlist = this.getById(songlistId);
        if (songlist == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "歌单不存在");
        }
        //删除关联表数据
        QueryWrapper<ReSonglistSong> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("creator_id", userId).eq("songlist_id", songlistId).in("song_id", songIdList);
        boolean isSongRemove = reSonglistSongService.remove(queryWrapper);
        if (!isSongRemove) {
            throw new BusinessException(StatusCode.DELETE_ERROR, "批量删除songlist-song关联表数据失败");
        }
        //更新歌单表数据
        UpdateWrapper<Songlist> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("creator_id", userId).eq("id", songlistId).setSql("song_count = song_count - " + songIdList.size());
        boolean isSonglistupdate = this.update(updateWrapper);
        if (!isSonglistupdate) {
            throw new BusinessException(StatusCode.UPDATE_ERROR, "更新歌单歌曲数量失败");
        }
        return true;
    }

    /**
     * 创建歌单评论
     *
     * @param songlistCommentCreateRequest
     * @return java.lang.Boolean
     * @date 2024/4/13  23:25
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createComment(SonglistCommentCreateRequest songlistCommentCreateRequest) {
        if (songlistCommentCreateRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long userId = songlistCommentCreateRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long songlistId = songlistCommentCreateRequest.getSonglistId();
        if (songlistId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //歌单是否存在
        Songlist songlist = this.getById(songlistId);
        if (songlist == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "歌单不存在");
        }
        String content = songlistCommentCreateRequest.getContent();
        if (StringUtils.isBlank(content)) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "内容不能为空");
        }
        //创建评论
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setFavourCount(0L);
        comment.setPublishTime(new Date());
        boolean isSonglistCommentSave = commentService.save(comment);
        if (!isSonglistCommentSave) {
            throw new BusinessException(StatusCode.CREATE_ERROR, "评论表创建评论失败");
        }
        //获取插入comment表数据的主键
        Long commentId = comment.getId();
        //songlist-comment关联表添加数据
        ReSonglistComment reSonglistComment = new ReSonglistComment();
        reSonglistComment.setUserId(userId);
        reSonglistComment.setSonglistId(songlistId);
        reSonglistComment.setCommentId(commentId);
        boolean isReSonglistCommentSave = reSonglistCommentService.save(reSonglistComment);
        if (!isReSonglistCommentSave) {
            throw new BusinessException(StatusCode.CREATE_ERROR, "创建songlist-comment关联表数据失败");
        }
        //更新歌单表数据
        UpdateWrapper<Songlist> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", songlistId).setSql("comment_count = comment_count + 1");
        boolean isSonglistupdate = this.update(updateWrapper);
        if (!isSonglistupdate) {
            throw new BusinessException(StatusCode.UPDATE_ERROR, "更新歌单评论数量失败");
        }
        return true;
    }

    /**
     * 根据歌单id获取歌单简介视图
     *
     * @param songlistId
     * @return com.dzy.model.vo.songlist.SonglistIntroVO
     * @date 2024/4/14  11:13
     */
    @Override
    public SonglistIntroVO getSonglistIntroVOById(Long songlistId) {
        if (songlistId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Songlist songlist = this.getById(songlistId);
        if (songlist == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        SonglistIntroVO songlistIntroVO = SonglistIntroVO.objToVO(songlist);
        //获取创建者姓名
        UserInfo userInfo = userInfoService.getById(songlist.getCreatorId());
        String nickname = userInfo.getNickname();
        songlistIntroVO.setCreatorName(nickname);
        return songlistIntroVO;
    }

    /**
     * 分页查询歌单的评论
     *
     * @param songlistCommentQueryRequest
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.dzy.model.vo.comment.CommentVO>
     * @date 2024/4/15  12:08
     */
    @Override
    public Page<CommentVO> listSonglistCommentByPage(SonglistCommentQueryRequest songlistCommentQueryRequest) {
        if (songlistCommentQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long songlistId = songlistCommentQueryRequest.getSonglistId();
        if (songlistId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        QueryWrapper<ReSonglistComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("songlist_id", songlistId).select("comment_id");
        //获取评论id列表
        List<Long> commentIdList = reSonglistCommentService.list(queryWrapper).stream().map(ReSonglistComment::getCommentId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(commentIdList)) {
            throw new BusinessException(StatusCode.DATAS_NULL_ERROR, "暂无评论");
        }
        //构造评论条件查询器
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.in("id", commentIdList);
        //分页对象
        int pageCurrent = songlistCommentQueryRequest.getPageCurrent();
        int pageSize = songlistCommentQueryRequest.getPageSize();
        Page<Comment> page = new Page<>(pageCurrent, pageSize);
        Page<Comment> commentPage = commentService.page(page, commentQueryWrapper);
        List<CommentVO> commentVOList = commentPage.getRecords().stream().map(comment -> commentService.getCommentVO(comment)).collect(Collectors.toList());
        Page<CommentVO> commentVOPage = new Page<>(pageCurrent, pageSize, commentPage.getTotal());
        commentVOPage.setRecords(commentVOList);
        return commentVOPage;
    }

}




