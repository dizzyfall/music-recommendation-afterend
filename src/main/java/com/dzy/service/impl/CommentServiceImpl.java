package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.CommentMapper;
import com.dzy.model.dto.comment.CommentCreateRequest;
import com.dzy.model.dto.comment.CommentQueryRequest;
import com.dzy.model.dto.comment.ReplyCreateRequest;
import com.dzy.model.entity.Comment;
import com.dzy.model.entity.ReSongComment;
import com.dzy.model.entity.Song;
import com.dzy.model.vo.comment.CommentVO;
import com.dzy.model.vo.userinfo.UserLoginVO;
import com.dzy.service.CommentService;
import com.dzy.service.ReSongCommentService;
import com.dzy.service.SongService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author DZY
 * @description 针对表【comment(用户评论表)】的数据库操作Service实现
 * @createDate 2024-04-05 10:40:39
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {

    @Autowired
    private ReSongCommentService reSongCommentService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private SongService songService;

    /**
     * 创建歌曲评论
     *
     * @param commentCreateRequest
     * @param loginUserVO
     * @return
     */
    //todo 是否和回复评论写一起
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createComment(CommentCreateRequest commentCreateRequest, UserLoginVO loginUserVO) {
        //todo 判断登录信息是否一致
        //校验
        Long songId = commentCreateRequest.getSongId();
        if (songId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //音乐是否存在
        Song isSongExist = songService.getById(songId);
        if (isSongExist == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "歌曲不存在");
        }
        Long createUserId = commentCreateRequest.getCreateUserId();
        if (createUserId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        String content = commentCreateRequest.getContent();
        if (StringUtils.isBlank(content)) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "内容不能为空");
        }
        Comment comment = new Comment();
        comment.setUserId(createUserId);
        comment.setContent(content);
        comment.setFavourCount(0L);
        comment.setPublishTime(new Date());
        boolean isUserCommentSave = commentService.save(comment);
        if (!isUserCommentSave) {
            throw new BusinessException(StatusCode.CREATE_ERROR, "创建评论失败");
        }
        //获取插入用户评论表的主键id
        Long commentId = comment.getId();
        //维护关联表
        ReSongComment reSongComment = new ReSongComment();
        reSongComment.setSongId(songId);
        reSongComment.setCommentId(commentId);
        reSongComment.setCreateUserId(createUserId);
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
     * @param replyCreateRequest
     * @param loginUserVO
     * @return
     */
    //todo 是否和回复评论写一起
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createReply(ReplyCreateRequest replyCreateRequest, UserLoginVO loginUserVO) {
        //todo 判断登录信息是否一致
        //校验
        Long songId = replyCreateRequest.getSongId();
        if (songId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //音乐是否存在
        Song isSongExist = songService.getById(songId);
        if (isSongExist == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "歌曲不存在");
        }
        //todo 评论是否存在
        Long commentId = replyCreateRequest.getCommentId();
        if (commentId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long createUserId = replyCreateRequest.getCreateUserId();
        if (createUserId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        String content = replyCreateRequest.getContent();
        if (StringUtils.isBlank(content)) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "内容不能为空");
        }
        Long receiverId = replyCreateRequest.getReceiverId();
        if (receiverId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long replierId = replyCreateRequest.getReplierId();
        if (replierId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Comment comment = new Comment();
        comment.setUserId(createUserId);
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

    /**
     * 分页查询自己的评论
     *
     * @param commentQueryRequest
     * @param loginUserVO
     * @return
     */
    @Override
    public Page<CommentVO> listMyCommentByPage(CommentQueryRequest commentQueryRequest, UserLoginVO loginUserVO) {
        Long userId = commentQueryRequest.getUserId();
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        int pageCurrent = commentQueryRequest.getPageCurrent();
        int pageSize = commentQueryRequest.getPageSize();
        Page<Comment> page = new Page<>(pageCurrent, pageSize);
        Page<Comment> commentPage = this.page(page, queryWrapper);
        List<CommentVO> commentVOList = commentPage.getRecords().stream().map(this::commentToCommentVO).collect(Collectors.toList());
        //新分页对象
        Page<CommentVO> commentVOPage = new Page<>(pageCurrent, pageSize, commentPage.getTotal());
        commentVOPage.setRecords(commentVOList);
        return commentVOPage;
    }

    /**
     * Comment转CommentVO对象
     *
     * @param comment
     * @return
     */
    @Override
    //todo 有问题的，引用类型时
    public CommentVO commentToCommentVO(Comment comment) {
        if (comment == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        CommentVO commentVO = new CommentVO();
        try {
            BeanUtils.copyProperties(comment, commentVO);
        } catch (BusinessException e) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "Bean复制属性错误");
        }
        return commentVO;
    }

}




