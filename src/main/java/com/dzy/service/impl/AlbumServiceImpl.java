package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.AlbumMapper;
import com.dzy.model.dto.album.AlbumCommentCreateRequest;
import com.dzy.model.dto.album.AlbumCommentQueryRequest;
import com.dzy.model.dto.album.AlbumQueryRequest;
import com.dzy.model.dto.album.AlbumSongQueryRequest;
import com.dzy.model.entity.Album;
import com.dzy.model.entity.Comment;
import com.dzy.model.entity.ReAlbumComment;
import com.dzy.model.entity.Song;
import com.dzy.model.vo.album.AlbumDetailVO;
import com.dzy.model.vo.album.AlbumIntroVO;
import com.dzy.model.vo.comment.CommentVO;
import com.dzy.model.vo.song.SongDetailVO;
import com.dzy.model.vo.song.SongIntroVO;
import com.dzy.service.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author DZY
 * @description 针对表【album(专辑表)】的数据库操作Service实现
 * @createDate 2024-04-07 13:26:21
 */
@Service
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album>
        implements AlbumService {

    @Resource
    private SongService songService;

    @Resource
    private CommentService commentService;

    @Resource
    private ReAlbumCommentService reAlbumCommentService;

    @Resource
    private SingerService singerService;

    @Resource
    private ReplyService replyService;


    /**
     * 分页查询歌手专辑
     *
     * @param albumQueryRequest
     * @return
     */
    @Override
    public Page<AlbumIntroVO> listAlbumByPage(AlbumQueryRequest albumQueryRequest) {
        Long singerId = albumQueryRequest.getSingerId();
        if (singerId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        QueryWrapper<Album> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("singer_id", singerId);
        int pageCurrent = albumQueryRequest.getPageCurrent();
        int pageSize = albumQueryRequest.getPageSize();
        Page<Album> page = new Page<>(pageCurrent, pageSize);
        Page<Album> albumPage = this.page(page, queryWrapper);
        List<AlbumIntroVO> albumIntroVOList = albumPage.getRecords().stream().map(this::getAlbumIntroVO).collect(Collectors.toList());
        Page<AlbumIntroVO> albumVOPage = new Page<>(pageCurrent, pageSize, albumPage.getTotal());
        albumVOPage.setRecords(albumIntroVOList);
        return albumVOPage;
    }

    /**
     * 获取专辑详情视图
     *
     * @param album
     * @return
     */
    @Override
    public AlbumDetailVO getAlbumDetailVO(Album album) {
        if (album == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        AlbumDetailVO albumDetailVO = new AlbumDetailVO();
        BeanUtils.copyProperties(album, albumDetailVO);
        //补充singerNameList属性
        albumDetailVO.setAlbumId(album.getId());
        List<String> singerNameList = singerService.getSingerNameList(album.getSingerIdList());
        albumDetailVO.setSingerNameList(singerNameList);
        String singerNameStr = singerService.getSingerNameStr(album.getSingerIdList());
        albumDetailVO.setSingerNameStr(singerNameStr);
        //补充songIntroVOList属性
        List<SongIntroVO> songIntroVOList = listSong(album.getId());
        albumDetailVO.setSongIntroVOList(songIntroVOList);
        return albumDetailVO;
    }

    /**
     * 获取专辑简介视图
     *
     * @param album
     * @return
     */
    @Override
    public AlbumIntroVO getAlbumIntroVO(Album album) {
        if (album == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        AlbumDetailVO albumDetailVO = getAlbumDetailVO(album);
        AlbumIntroVO albumIntroVO = new AlbumIntroVO();
        BeanUtils.copyProperties(albumDetailVO, albumIntroVO);
        return albumIntroVO;
    }

    /**
     * 查询指定专辑详情
     *
     * @param albumSongQueryRequest
     * @return
     */
    @Override
    public AlbumDetailVO searchAlbumDetail(AlbumSongQueryRequest albumSongQueryRequest) {
//        Long singerId = albumSongQueryRequest.getSingerId();
//        if (singerId == null) {
//            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
//        }
        Long albumId = albumSongQueryRequest.getAlbumId();
        if (albumId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        QueryWrapper<Album> queryWrapper = new QueryWrapper<>();
        //queryWrapper.eq("id", albumId).eq("singer_id", singerId);
        queryWrapper.eq("id", albumId);
        Album album = this.getOne(queryWrapper);
        if (album == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "歌手无此专辑");
        }
        return getAlbumDetailVO(album);
    }

    /**
     * 查询指定专辑所有歌曲
     *
     * @param albumSongQueryRequest
     * @return
     */
    @Override
    public List<SongIntroVO> listSong(AlbumSongQueryRequest albumSongQueryRequest) {
//        Long singerId = albumSongQueryRequest.getSingerId();
//        if (singerId == null) {
//            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
//        }
        Long albumId = albumSongQueryRequest.getAlbumId();
        if (albumId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        QueryWrapper<Album> queryWrapper = new QueryWrapper<>();
        //queryWrapper.eq("id", albumId).eq("singer_id", singerId);
        queryWrapper.eq("id", albumId);
        Album album = this.getOne(queryWrapper);
        if (album == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "歌手无此专辑");
        }
        //查询专辑全部歌曲
        QueryWrapper<Song> songQueryWrapper = new QueryWrapper<>();
        songQueryWrapper.eq("album_id", albumId);
        List<Song> songList = songService.list(songQueryWrapper);
        return songList.stream().map(song -> songService.getSongIntroVO(song)).collect(Collectors.toList());
    }

    /**
     * 查询指定专辑所有歌曲
     *
     * @param albumId
     * @return
     */
    public List<SongIntroVO> listSong(Long albumId) {
        if (albumId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //查询专辑全部歌曲
        QueryWrapper<Song> songQueryWrapper = new QueryWrapper<>();
        songQueryWrapper.eq("album_id", albumId);
        List<Song> songList = songService.list(songQueryWrapper);
        return songList.stream().map(song -> songService.getSongIntroVO(song)).collect(Collectors.toList());
    }

    /**
     * 创建专辑评论
     *
     * @param albumCommentCreateRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createComment(AlbumCommentCreateRequest albumCommentCreateRequest) {
        if (albumCommentCreateRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long userId = albumCommentCreateRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long albumId = albumCommentCreateRequest.getAlbumId();
        if (albumId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Album album = this.getById(albumId);
        if (album == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "专辑不存在");
        }
        String content = albumCommentCreateRequest.getContent();
        if (StringUtils.isBlank(content)) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "内容不能为空");
        }
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setFavourCount(0L);
        comment.setPublishTime(new Date());
        boolean isAlbumCommentSave = commentService.save(comment);
        if (!isAlbumCommentSave) {
            throw new BusinessException(StatusCode.CREATE_ERROR);
        }
        //获取插入comment表数据的主键
        Long commentId = comment.getId();
        //维护关系表
        ReAlbumComment reAlbumComment = new ReAlbumComment();
        reAlbumComment.setAlbumId(albumId);
        reAlbumComment.setCommentId(commentId);
        boolean isReAlbumCommentSave = reAlbumCommentService.save(reAlbumComment);
        if (!isReAlbumCommentSave) {
            throw new BusinessException(StatusCode.CREATE_ERROR);
        }
        return true;
    }

    /**
     * 分页查询专辑的评论
     *
     * @param albumCommentQueryRequest
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.dzy.model.vo.comment.CommentVO>
     * @date 2024/4/15  9:38
     */
    @Override
    public Page<CommentVO> listAlbumCommentByPage(AlbumCommentQueryRequest albumCommentQueryRequest) {
        if (albumCommentQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long albumId = albumCommentQueryRequest.getAlbumId();
        if (albumId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        QueryWrapper<ReAlbumComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("album_id", albumId).select("comment_id");
        //获取评论id列表
        List<Long> commentIdList = reAlbumCommentService.list(queryWrapper).stream().map(ReAlbumComment::getCommentId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(commentIdList)) {
            throw new BusinessException(StatusCode.DATAS_NULL_ERROR, "暂无评论");
        }
        //构造评论条件查询器
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.in("id", commentIdList);
        //分页对象
        int pageCurrent = albumCommentQueryRequest.getPageCurrent();
        int pageSize = albumCommentQueryRequest.getPageSize();
        Page<Comment> page = new Page<>(pageCurrent, pageSize);
        Page<Comment> commentPage = commentService.page(page, commentQueryWrapper);
        List<CommentVO> commentVOList = commentPage.getRecords().stream().map(comment -> commentService.getCommentVO(comment)).collect(Collectors.toList());
        Page<CommentVO> commentVOPage = new Page<>(pageCurrent, pageSize, commentPage.getTotal());
        commentVOPage.setRecords(commentVOList);
        return commentVOPage;
    }

}




