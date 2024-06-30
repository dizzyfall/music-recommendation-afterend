package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.CommentMapper;
import com.dzy.model.dto.comment.CommentDeleteRequest;
import com.dzy.model.dto.comment.CommentQueryRequest;
import com.dzy.model.entity.Comment;
import com.dzy.model.entity.ReAlbumComment;
import com.dzy.model.entity.ReSongComment;
import com.dzy.model.entity.ReSonglistComment;
import com.dzy.model.enums.CommentTypeEnum;
import com.dzy.model.vo.comment.CommentVO;
import com.dzy.model.vo.userinfo.UserInfoIntroVO;
import com.dzy.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

    @Resource
    private ReSongCommentService reSongCommentService;

    @Resource
    private ReAlbumCommentService reAlbumCommentService;

    @Resource
    private ReSonglistCommentService reSonglistCommentService;

    @Resource
    private UserInfoService userInfoService;

    /**
     * 分页查询自己的评论
     *
     * @param commentQueryRequest
     * @return
     */
    @Override
    public Page<CommentVO> listMyCommentByPage(CommentQueryRequest commentQueryRequest) {
        Long userId = commentQueryRequest.getUserId();
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        int pageCurrent = commentQueryRequest.getPageCurrent();
        int pageSize = commentQueryRequest.getPageSize();
        Page<Comment> page = new Page<>(pageCurrent, pageSize);
        Page<Comment> commentPage = this.page(page, queryWrapper);
        List<CommentVO> commentVOList = commentPage.getRecords().stream().map(this::getCommentVO).collect(Collectors.toList());
        //新分页对象
        Page<CommentVO> commentVOPage = new Page<>(pageCurrent, pageSize, commentPage.getTotal());
        commentVOPage.setRecords(commentVOList);
        return commentVOPage;
    }

    /**
     * 获取CommentVO视图对象
     *
     * @param comment
     * @return com.dzy.model.vo.comment.CommentVO
     * @date 2024/4/15  21:44
     */
    @Override
    public CommentVO getCommentVO(Comment comment) {
        if (comment == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        CommentVO commentVO = CommentVO.objToVO(comment);
        commentVO.setCommentId(comment.getId());
        UserInfoIntroVO userInfoIntroVO = userInfoService.getUserInfoIntroVOById(comment.getUserId());
        commentVO.setUserInfoIntroVO(userInfoIntroVO);
        return commentVO;
    }

    /**
     * 删除自己的评论
     *
     * @param commentDeleteRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteMyComment(CommentDeleteRequest commentDeleteRequest) {
        Long userId = commentDeleteRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long commentId = commentDeleteRequest.getCommentId();
        if (commentId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //自己评论是否存在
        Comment comment = this.getById(commentId);
        if (comment == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "评论不存在");
        }
        //删除自己的歌曲评论,回复保留
        CommentTypeEnum commentTypeEnum = getCommentTypeById(commentId);
        Boolean isDeleteComment = deleteCommentByCommentTypeEum(commentTypeEnum, commentId);
        if (!isDeleteComment) {
            throw new BusinessException(StatusCode.DELETE_ERROR);
        }
        //删除评论表数据
        boolean isRemove = this.removeById(commentId);
        if (!isRemove) {
            throw new BusinessException(StatusCode.DELETE_ERROR);
        }
        return true;
    }

    /**
     * 根据评论id获取评论类型
     *
     * @param commentId
     * @return com.dzy.model.enums.CommentTypeEum
     * @date 2024/4/18  12:20
     */
    @Override
    public CommentTypeEnum getCommentTypeById(Long commentId) {
        QueryWrapper<ReSongComment> reSongCommentQueryWrapper = new QueryWrapper<>();
        reSongCommentQueryWrapper.eq("comment_id", commentId);
        ReSongComment reSongCommentOne = reSongCommentService.getOne(reSongCommentQueryWrapper);
        if (reSongCommentOne != null) {
            return CommentTypeEnum.SONG_TYPE;
        }
        QueryWrapper<ReAlbumComment> reAlbumCommentQueryWrapper = new QueryWrapper<>();
        reAlbumCommentQueryWrapper.eq("comment_id", commentId);
        ReAlbumComment reAlbumCommentOne = reAlbumCommentService.getOne(reAlbumCommentQueryWrapper);
        if (reAlbumCommentOne != null) {
            return CommentTypeEnum.ALBUM_TYPE;
        }
        QueryWrapper<ReSonglistComment> reSonglistCommentQueryWrapper = new QueryWrapper<>();
        reSonglistCommentQueryWrapper.eq("comment_id", commentId);
        ReSonglistComment reSonglistCommentOne = reSonglistCommentService.getOne(reSonglistCommentQueryWrapper);
        if (reSonglistCommentOne != null) {
            return CommentTypeEnum.SONGLIST_TYPE;
        }
        return null;
    }

    /**
     * 根据评论类型删除对应评论表数据
     *
     * @param commentTypeEnum
     * @param commentId
     * @return java.lang.Boolean
     * @date 2024/4/18  12:30
     */
    @Override
    public Boolean deleteCommentByCommentTypeEum(CommentTypeEnum commentTypeEnum, Long commentId) {
        if (commentTypeEnum == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "评论不存在");
        }
        //评论是歌曲类型
        if ("song".equals(commentTypeEnum.getValue())) {
            QueryWrapper<ReSongComment> deleteQueryWrapper = new QueryWrapper<>();
            deleteQueryWrapper.eq("comment_id", commentId);
            //删关联表数据
            boolean isRemoveReSongComment = reSongCommentService.remove(deleteQueryWrapper);
            if (!isRemoveReSongComment) {
                throw new BusinessException(StatusCode.DELETE_ERROR);
            }
        } else if ("album".equals(commentTypeEnum.getValue())) {
            QueryWrapper<ReAlbumComment> deleteQueryWrapper = new QueryWrapper<>();
            deleteQueryWrapper.eq("comment_id", commentId);
            //删关联表数据
            boolean isRemoveReAlbumComment = reAlbumCommentService.remove(deleteQueryWrapper);
            if (!isRemoveReAlbumComment) {
                throw new BusinessException(StatusCode.DELETE_ERROR);
            }
        } else if ("songlist".equals(commentTypeEnum.getValue())) {
            QueryWrapper<ReSonglistComment> deleteQueryWrapper = new QueryWrapper<>();
            deleteQueryWrapper.eq("comment_id", commentId);
            //删关联表数据
            boolean isRemoveReSonglistComment = reSonglistCommentService.remove(deleteQueryWrapper);
            if (!isRemoveReSonglistComment) {
                throw new BusinessException(StatusCode.DELETE_ERROR);
            }
        }
        return true;
    }

}




