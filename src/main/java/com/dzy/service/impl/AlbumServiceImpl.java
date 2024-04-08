package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.AlbumMapper;
import com.dzy.model.dto.album.AlbumCommentCreateRequest;
import com.dzy.model.dto.album.AlbumQueryRequest;
import com.dzy.model.dto.album.AlbumSongQueryRequest;
import com.dzy.model.entity.Album;
import com.dzy.model.entity.Comment;
import com.dzy.model.entity.ReAlbumComment;
import com.dzy.model.entity.Song;
import com.dzy.model.vo.album.AlbumInfoVO;
import com.dzy.model.vo.album.AlbumSongVO;
import com.dzy.model.vo.album.AlbumVO;
import com.dzy.model.vo.song.SongVO;
import com.dzy.service.AlbumService;
import com.dzy.service.CommentService;
import com.dzy.service.ReAlbumCommentService;
import com.dzy.service.SongService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SongService songService;

    @Resource
    private CommentService commentService;

    @Resource
    private ReAlbumCommentService reAlbumCommentService;

    /**
     * 分页查询歌手专辑
     *
     * @param albumQueryRequest
     * @return
     */
    @Override
    public Page<AlbumVO> listAlbumByPage(AlbumQueryRequest albumQueryRequest) {
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
        List<AlbumVO> albumVOList = albumPage.getRecords().stream().map(this::albumToAlbumVO).collect(Collectors.toList());
        Page<AlbumVO> albumVOPage = new Page<>(pageCurrent, pageSize, albumPage.getTotal());
        albumVOPage.setRecords(albumVOList);
        return albumVOPage;
    }

    /**
     * Album转AlbumVO
     *
     * @param album
     * @return
     */
    @Override
    public AlbumVO albumToAlbumVO(Album album) {
        if (album == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        AlbumVO albumVO = new AlbumVO();
        try {
            BeanUtils.copyProperties(album, albumVO);
        } catch (BusinessException e) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "Bean复制属性错误");
        }
        return albumVO;
    }

    /**
     * Album转AlbumInfoVO
     *
     * @param album
     * @return
     */
    @Override
    public AlbumInfoVO albumToAlbumInfoVO(Album album) {
        if (album == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        AlbumInfoVO albumInfoVO = new AlbumInfoVO();
        try {
            BeanUtils.copyProperties(album, albumInfoVO);
        } catch (BusinessException e) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "Bean复制属性错误");
        }
        return albumInfoVO;
    }

    /**
     * 歌曲转专辑歌曲简介视图
     *
     * @param song
     * @return
     */
    public AlbumSongVO getAlbumSongVO(Song song) {
        if (song == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        AlbumSongVO albumSongVO = new AlbumSongVO();
        SongVO songVO = songService.getSongVO(song);
        albumSongVO.setTitle(songVO.getTitle());
        albumSongVO.setSingerNameList(songVO.getSingerNameList());
        return albumSongVO;
    }

    /**
     * 查询指定专辑详情
     *
     * @param albumSongQueryRequest
     * @return
     */
    @Override
    public AlbumInfoVO searchAlbumInfo(AlbumSongQueryRequest albumSongQueryRequest) {
        Long singerId = albumSongQueryRequest.getSingerId();
        if (singerId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long albumId = albumSongQueryRequest.getAlbumId();
        if (albumId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        QueryWrapper<Album> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", albumId).eq("singer_id", singerId);
        Album album = this.getOne(queryWrapper);
        if (album == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "歌手无此专辑");
        }
        return albumToAlbumInfoVO(album);
    }

    /**
     * 查询指定专辑所有歌曲
     *
     * @param albumSongQueryRequest
     * @return
     */
    @Override
    public List<AlbumSongVO> listSong(AlbumSongQueryRequest albumSongQueryRequest) {
        Long singerId = albumSongQueryRequest.getSingerId();
        if (singerId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long albumId = albumSongQueryRequest.getAlbumId();
        if (albumId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        QueryWrapper<Album> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", albumId).eq("singer_id", singerId);
        Album album = this.getOne(queryWrapper);
        if (album == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "歌手无此专辑");
        }
        //查询专辑全部歌曲
        QueryWrapper<Song> songQueryWrapper = new QueryWrapper<>();
        songQueryWrapper.eq("album_id", albumId);
        List<Song> songList = songService.list(songQueryWrapper);
        return songList.stream().map(this::getAlbumSongVO).collect(Collectors.toList());
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

}



