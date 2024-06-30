package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.SongMapper;
import com.dzy.model.dto.song.SongCommentCreateRequest;
import com.dzy.model.dto.song.SongCommentQueryRequest;
import com.dzy.model.dto.song.SongDetailQueryRequest;
import com.dzy.model.dto.song.SongQueryRequest;
import com.dzy.model.entity.Comment;
import com.dzy.model.entity.ReSongComment;
import com.dzy.model.entity.Singer;
import com.dzy.model.entity.Song;
import com.dzy.model.enums.songtags.GenreEnum;
import com.dzy.model.enums.songtags.LangEnum;
import com.dzy.model.vo.comment.CommentVO;
import com.dzy.model.vo.song.SongDetailVO;
import com.dzy.model.vo.song.SongIntroVO;
import com.dzy.service.*;
import org.apache.commons.collections4.CollectionUtils;
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
 * @description 针对表【song(歌曲表)】的数据库操作Service实现
 * @createDate 2024-04-03 11:40:52
 */
@Service
public class SongServiceImpl extends ServiceImpl<SongMapper, Song>
        implements SongService {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ReSongCommentService reSongCommentService;

    @Autowired
    private SingerService singerService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private ReplyService replyService;

    /**
     * Song转SongDetailVO
     * 不能简单使用属性复制，因为SongVO还有Song中没有的属性
     *
     * @param song
     * @return
     */
    @Override
    public SongDetailVO getSongDetailVO(Song song) {
        //原本属性
        SongDetailVO songDetailVO = SongDetailVO.objToVO(song);
        //补充属性
        String singerListId = song.getSingerListId();
        List<String> singerNameList = singerService.getSingerNameList(singerListId);
        String singerNameStr = singerService.getSingerNameStr(singerListId);
        songDetailVO.setSingerNameList(singerNameList);
        songDetailVO.setSingerNameStr(singerNameStr);
        songDetailVO.setSongId(song.getId());
        songDetailVO.setLang(LangEnum.getLangTagNameById(song.getLang()));
        songDetailVO.setGenre(GenreEnum.getGenreTagNameById(song.getGenre()));
        return songDetailVO;
    }

    /**
     * 获取歌曲详情
     *
     * @param songId
     * @return com.dzy.model.vo.song.SongDetailVO
     * @date 2024/4/17  18:23
     */
    @Override
    public SongDetailVO getSongDetailVOById(Long songId) {
        if (songId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Song song = this.getById(songId);
        if (song == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "无此歌曲");
        }
        return getSongDetailVO(song);
    }

    /**
     * 获取歌曲简介
     *
     * @param song
     * @return
     */
    @Override
    public SongIntroVO getSongIntroVO(Song song) {
        if (song == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        SongDetailVO songDetailVO = getSongDetailVO(song);
        SongIntroVO songIntroVO = new SongIntroVO();
        BeanUtils.copyProperties(songDetailVO, songIntroVO);
        return songIntroVO;
    }

    /**
     * 通过歌曲Id获取歌曲简介
     *
     * @param songId
     * @return
     */
    @Override
    public SongIntroVO getSongIntroVOById(Long songId) {
        if (songId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Song song = this.getById(songId);
        if (song == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "无此歌曲");
        }
        return getSongIntroVO(song);
    }

    /**
     * 创建歌曲评论
     *
     * @param songCommentCreateRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createComment(SongCommentCreateRequest songCommentCreateRequest) {
        if (songCommentCreateRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long userId = songCommentCreateRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long songId = songCommentCreateRequest.getSongId();
        if (songId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //音乐是否存在
        Song song = this.getById(songId);
        if (song == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "歌曲不存在");
        }
        String content = songCommentCreateRequest.getContent();
        if (StringUtils.isBlank(content)) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "内容不能为空");
        }
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setFavourCount(0L);
        comment.setPublishTime(new Date());
        boolean isSongCommentSave = commentService.save(comment);
        if (!isSongCommentSave) {
            throw new BusinessException(StatusCode.CREATE_ERROR, "创建评论失败");
        }
        //获取插入用户评论表的主键id
        Long commentId = comment.getId();
        //维护关联表
        ReSongComment reSongComment = new ReSongComment();
        reSongComment.setUserId(userId);
        reSongComment.setSongId(songId);
        reSongComment.setCommentId(commentId);
        boolean isReSongCommentSave = reSongCommentService.save(reSongComment);
        if (!isReSongCommentSave) {
            throw new BusinessException(StatusCode.CREATE_ERROR, "创建评论失败");
        }
        //更新歌曲表数据
        UpdateWrapper<Song> songUpdateWrapper = new UpdateWrapper<>();
        songUpdateWrapper.eq("id", songId).setSql("comment_count = comment_count + 1");
        boolean isCommentUpdate = this.update(songUpdateWrapper);
        return true;
    }

    /**
     * 分页查询歌曲的评论
     *
     * @param songCommentQueryRequest
     * @return
     */
    @Override
    public Page<CommentVO> listSongCommentByPage(SongCommentQueryRequest songCommentQueryRequest) {
        if (songCommentQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long songId = songCommentQueryRequest.getSongId();
        if (songId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        QueryWrapper<ReSongComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("song_id", songId).select("comment_id");
        //获取评论id列表
        List<Long> commentIdList = reSongCommentService.list(queryWrapper).stream().map(ReSongComment::getCommentId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(commentIdList)) {
            throw new BusinessException(StatusCode.DATAS_NULL_ERROR, "暂无评论");
        }
        //构造评论条件查询器
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.in("id", commentIdList);
        //分页对象
        int pageCurrent = songCommentQueryRequest.getPageCurrent();
        int pageSize = songCommentQueryRequest.getPageSize();
        Page<Comment> page = new Page<>(pageCurrent, pageSize);
        Page<Comment> commentPage = commentService.page(page, commentQueryWrapper);
        List<CommentVO> commentVOList = commentPage.getRecords().stream().map(comment -> commentService.getCommentVO(comment)).collect(Collectors.toList());
        Page<CommentVO> commentVOPage = new Page<>(pageCurrent, pageSize, commentPage.getTotal());
        commentVOPage.setRecords(commentVOList);
        return commentVOPage;
    }

    /**
     * 分页查询歌手歌曲
     *
     * @param songQueryRequest
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.dzy.model.vo.song.SongIntroVO>
     * @date 2024/4/30  23:19
     */
    @Override
    public Page<SongIntroVO> listSongByPage(SongQueryRequest songQueryRequest) {
        if (songQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long singerId = songQueryRequest.getSingerId();
        if (singerId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //判断歌手是否存在
        Singer singer = singerService.getById(singerId);
        if (singer == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "暂无此歌手");
        }
        //todo 以后再来优化 由于字段设计有一点问题 如果singerId是1，则转为"1"进行模糊查询
        QueryWrapper<Song> queryWrapper = new QueryWrapper<>();
        String singerIdStr = "\"" + singerId + "\"";
        queryWrapper.like("singer_list_id", singerIdStr);
        int pageCurrent = songQueryRequest.getPageCurrent();
        int pageSize = songQueryRequest.getPageSize();
        Page<Song> page = new Page<>(pageCurrent, pageSize);
        Page<Song> songPage = this.page(page, queryWrapper);
        List<SongIntroVO> songIntroVOList = songPage.getRecords().stream().map(this::getSongIntroVO).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(songIntroVOList)) {
            throw new BusinessException(StatusCode.DATAS_NULL_ERROR, "暂无歌曲");
        }
        Page<SongIntroVO> songIntroVOPage = new Page<>(pageCurrent, pageSize, songPage.getTotal());
        songIntroVOPage.setRecords(songIntroVOList);
        return songIntroVOPage;
    }

    /**
     * 查询指定歌曲详情
     *
     * @param songDetailQueryRequest
     * @return com.dzy.model.vo.song.SongDetailVO
     * @date 2024/5/18  12:18
     */
    @Override
    public SongDetailVO searchSongDetail(SongDetailQueryRequest songDetailQueryRequest) {
        Long songId = songDetailQueryRequest.getSongId();
        if (songId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        QueryWrapper<Song> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", songId);
        Song song = this.getOne(queryWrapper);
        if (song == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "歌手无此歌曲");
        }
        return getSongDetailVO(song);
    }

}





