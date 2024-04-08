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
import com.dzy.model.entity.ReSongComment;
import com.dzy.model.vo.comment.CommentVO;
import com.dzy.model.vo.userinfo.UserLoginVO;
import com.dzy.service.CommentService;
import com.dzy.service.ReSongCommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 删除自己的评论
     *
     * @param commentDeleteRequest
     * @param loginUserVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteMySongComment(CommentDeleteRequest commentDeleteRequest, UserLoginVO loginUserVO) {
        Long userId = commentDeleteRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long commentId = commentDeleteRequest.getCommentId();
        if (commentId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //自己评论是否存在
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", commentId).eq("user_id", userId);
        Comment comment = this.getOne(queryWrapper);
        if (comment == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        //删除自己评论歌曲的评论，而不是回复他人的评论
        //TODO 删除回复他人的评论之后实现
        QueryWrapper<ReSongComment> deleteQueryWrapper = new QueryWrapper<>();
        deleteQueryWrapper.eq("comment_id", commentId);
        //删关联表数据
        boolean isRemoveRe = reSongCommentService.remove(deleteQueryWrapper);
        if (!isRemoveRe) {
            throw new BusinessException(StatusCode.DELETE_ERROR);
        }
        //删除评论表数据
        boolean isRemove = this.removeById(commentId);
        if (!isRemove) {
            throw new BusinessException(StatusCode.DELETE_ERROR);
        }
        return true;
    }

}




