package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.CommentMapper;
import com.dzy.model.dto.comment.CommentQueryRequest;
import com.dzy.model.entity.Comment;
import com.dzy.model.vo.comment.CommentVO;
import com.dzy.model.vo.userinfo.UserLoginVO;
import com.dzy.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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

    /**
     * 查询自己所有的评论
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




