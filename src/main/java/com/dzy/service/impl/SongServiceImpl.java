package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.CommentConstant;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.SongMapper;
import com.dzy.model.dto.song.SongCommentCreateRequest;
import com.dzy.model.dto.song.SongCommentQueryRequest;
import com.dzy.model.dto.song.SongReplyCreateRequest;
import com.dzy.model.dto.song.SongReplyQueryRequest;
import com.dzy.model.entity.Comment;
import com.dzy.model.entity.ReSongComment;
import com.dzy.model.entity.Reply;
import com.dzy.model.entity.Song;
import com.dzy.model.vo.comment.CommentVO;
import com.dzy.model.vo.reply.ReplyVO;
import com.dzy.model.vo.song.SongDetailVO;
import com.dzy.model.vo.song.SongIntroVO;
import com.dzy.service.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
    private ReSongReplyService reSongReplyService;

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
    public SongDetailVO getSongDetailById(Long songId) {
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
        SongDetailVO songDetailVO = getSongDetailById(songId);
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
     * 创建歌曲评论回复
     *
     * @param songReplyCreateRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createReply(SongReplyCreateRequest songReplyCreateRequest) {
        if (songReplyCreateRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long userId = songReplyCreateRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //评论是否存在
        Long commentId = songReplyCreateRequest.getCommentId();
        if (commentId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Comment comment = commentService.getById(commentId);
        if (comment == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "评论不存在");
        }
        String content = songReplyCreateRequest.getContent();
        if (StringUtils.isBlank(content)) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "内容不能为空");
        }
        //todo 判断要回复的人存在存不在这个歌曲评论里面
        Long receiverId = songReplyCreateRequest.getReceiverId();
        if (receiverId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        String commentType = songReplyCreateRequest.getCommentType();
        if (StringUtils.isBlank(commentType)) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //评论类型是否符合
        if (!commentType.equals(CommentConstant.SONG_TYPE)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "回复的评论不是歌曲类型");
        }
        Reply reply = new Reply();
        reply.setUserId(userId);
        reply.setCommentId(commentId);
        reply.setReceiverId(receiverId);
        reply.setContent(content);
        reply.setPublishTime(new Date());
        reply.setCommentType(commentType);
        boolean isSongReplySave = replyService.save(reply);
        if (!isSongReplySave) {
            throw new BusinessException(StatusCode.CREATE_ERROR, "创建回复失败");
        }
//        //获取插入回复表的id
//        Long replyId = reply.getId();
//        //更新关联表数据
//        //通过评论id和评论类型获取歌曲id
//        QueryWrapper<ReSongComment> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("comment_id",commentId);
//        ReSongComment reSongComment = reSongCommentService.getOne(queryWrapper);
//        Long songId = reSongComment.getSongId();
//        //更新关联表数据
//        ReSongReply reSongReply = new ReSongReply();
//        reSongReply.setUserId(userId);
//        reSongReply.setSongId(songId);
//        reSongReply.setCommentId(commentId);
//        reSongReply.setReplyId(replyId);
//        boolean isReSongReplySave = reSongReplyService.save(reSongReply);
//        if (!isReSongReplySave) {
//            throw new BusinessException(StatusCode.CREATE_ERROR, "创建回复失败");
//        }
        return true;
    }

    /**
     * 分页查询歌曲指定评论的回复
     *
     * @param songReplyQueryRequest
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.dzy.model.vo.reply.ReplyVO>
     * @date 2024/4/16  15:17
     */
    @Override
    public Page<ReplyVO> listSongReplyByPage(SongReplyQueryRequest songReplyQueryRequest) {
        if (songReplyQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long commentId = songReplyQueryRequest.getCommentId();
        if (commentId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        QueryWrapper<Reply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comment_id", commentId).orderByAsc("publish_time");
        int pageCurrent = songReplyQueryRequest.getPageCurrent();
        int pageSize = songReplyQueryRequest.getPageSize();
        Page<Reply> page = new Page<>(pageCurrent, pageSize);
        Page<Reply> replyPage = replyService.page(page, queryWrapper);
        List<ReplyVO> replyVOList = replyPage.getRecords().stream().map(reply -> replyService.getReplyVO(reply)).collect(Collectors.toList());
        Page<ReplyVO> replyVOPage = new Page<>(pageCurrent, pageSize, replyPage.getTotal());
        replyVOPage.setRecords(replyVOList);
        return replyVOPage;
    }

}





