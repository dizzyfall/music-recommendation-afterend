package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.SongMapper;
import com.dzy.model.dto.song.SongCommentCreateRequest;
import com.dzy.model.dto.song.SongCommentQueryRequest;
import com.dzy.model.dto.song.SongReplyCreateRequest;
import com.dzy.model.entity.Comment;
import com.dzy.model.entity.ReSongComment;
import com.dzy.model.entity.Song;
import com.dzy.model.vo.comment.CommentVO;
import com.dzy.model.vo.song.SongDetailVO;
import com.dzy.model.vo.song.SongIntroVO;
import com.dzy.service.CommentService;
import com.dzy.service.ReSongCommentService;
import com.dzy.service.SingerService;
import com.dzy.service.SongService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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

    @Resource
    private SongMapper songMapper;

    @Autowired
    private ReSongCommentService reSongCommentService;

    @Autowired
    private SingerService singerService;

    /**
     * 分页查询歌曲的评论
     *
     * @param songCommentQueryRequest
     * @return
     */
    @Override
    public Page<CommentVO> listSongCommentByPage(SongCommentQueryRequest songCommentQueryRequest) {
        Long songId = songCommentQueryRequest.getSongId();
        if (songId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        QueryWrapper<ReSongComment> queryWrapper = new QueryWrapper<>();
        //只查询需要的字段
        //List<String> columnList = Arrays.asList("id","comment_id","create_user_id","receiver_id","replier_id");
        ////查询关联表数据
        //queryWrapper.eq("song_id",songId).select(columnList);
        queryWrapper.eq("song_id", songId).select("comment_id");
        //获取评论id列表
        List<ReSongComment> commentIdList = reSongCommentService.list(queryWrapper);
        //构造评论条件查询器
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        //commentQueryWrapper.in("id", commentIdList);
        //分页对象
        int pageCurrent = songCommentQueryRequest.getPageCurrent();
        int pageSize = songCommentQueryRequest.getPageSize();
        Page<Comment> page = new Page<>(pageCurrent, pageSize);
        Page<CommentVO> commentPage = songMapper.listCommentByPage(page, commentQueryWrapper);
        //脱敏
        //List<CommentVO> commentVOList = commentPage.getRecords().stream().map(comment -> commentService.commentToCommentVO(comment)).collect(Collectors.toList());
        List<CommentVO> commentVOList = commentPage.getRecords();
        Page<CommentVO> commentVOPage = new Page<>(pageCurrent, pageSize, commentPage.getTotal());
        commentVOPage.setRecords(commentVOList);
        return commentVOPage;
    }

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
        songDetailVO.setSingerNameList(singerNameList);
        return songDetailVO;
    }

    /**
     * 获取歌曲详情
     *
     * @param songId
     * @return
     */
    @Override
    public SongDetailVO getSongDetail(Long songId) {
        if (songId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        QueryWrapper<Song> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", songId);
        Song song = this.getOne(queryWrapper);
        return getSongDetailVO(song);
    }

    /**
     * 获取歌曲简介
     *
     * @param song
     * @return
     */
    //todo 查询优化
    @Override
    public SongIntroVO getSongIntro(Song song) {
        if (song == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        SongDetailVO songDetailVO = getSongDetailVO(song);
        SongIntroVO songIntroVO = new SongIntroVO();
        songIntroVO.setTitle(songDetailVO.getTitle());
        songIntroVO.setSingerNameList(songDetailVO.getSingerNameList());
        return songIntroVO;
    }

    /**
     * 通过歌曲Id获取歌曲简介
     *
     * @param songId
     * @return
     */
    @Override
    public SongIntroVO getSongIntroById(Long songId) {
        if (songId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        SongDetailVO songDetailVO = getSongDetail(songId);
        SongIntroVO songIntroVO = new SongIntroVO();
        songIntroVO.setTitle(songDetailVO.getTitle());
        songIntroVO.setSingerNameList(songDetailVO.getSingerNameList());
        return songIntroVO;
    }


    /**
     * 创建歌曲评论
     *
     * @param songCommentCreateRequest
     * @return
     */
    //todo 是否和回复评论写一起
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
        reSongComment.setSongId(songId);
        reSongComment.setCommentId(commentId);
        reSongComment.setCreateUserId(userId);
        //followerId为-1,replyUserId为-1
        reSongComment.setReceiverId(-1L);
        reSongComment.setReplierId(-1L);
        boolean isReSongCommentSave = reSongCommentService.save(reSongComment);
        if (!isReSongCommentSave) {
            throw new BusinessException(StatusCode.CREATE_ERROR, "创建评论失败");
        }
        return true;
    }

    /**
     * 创建歌曲评论回复
     *
     * @param songReplyCreateRequest
     * @return
     */
    //todo 是否和回复评论写一起
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createReply(SongReplyCreateRequest songReplyCreateRequest) {
        if (songReplyCreateRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long songId = songReplyCreateRequest.getSongId();
        if (songId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //音乐是否存在
        Song song = this.getById(songId);
        if (song == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "歌曲不存在");
        }
        //todo 评论是否存在
        Long commentId = songReplyCreateRequest.getCommentId();
        if (commentId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long userId = songReplyCreateRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        String content = songReplyCreateRequest.getContent();
        if (StringUtils.isBlank(content)) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "内容不能为空");
        }
        Long receiverId = songReplyCreateRequest.getReceiverId();
        if (receiverId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long replierId = songReplyCreateRequest.getReplierId();
        if (replierId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setFavourCount(0L);
        comment.setPublishTime(new Date());
        boolean isUserCommentSave = commentService.save(comment);
        if (!isUserCommentSave) {
            throw new BusinessException(StatusCode.CREATE_ERROR, "创建评论失败");
        }
        Long newCommentId = comment.getId();
        QueryWrapper<ReSongComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comment_id", commentId);
        ReSongComment oldReSongComment = reSongCommentService.getOne(queryWrapper);
        ReSongComment newReSongComment = new ReSongComment();
        //todo 这里的songId和通过userCmtId查询的数据的songId是否一样,同理其他字段
        newReSongComment.setSongId(songId);
        //todo 有问题?
        newReSongComment.setCommentId(newCommentId);
        newReSongComment.setCreateUserId(oldReSongComment.getCreateUserId());
        newReSongComment.setReceiverId(receiverId);
        newReSongComment.setReplierId(replierId);
        boolean isReSongCommentSave = reSongCommentService.save(newReSongComment);
        if (!isReSongCommentSave) {
            throw new BusinessException(StatusCode.CREATE_ERROR, "创建评论失败");
        }
        return true;
    }

}




