package com.dzy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.model.dto.comment.CommentCreateRequest;
import com.dzy.model.dto.comment.CommentDeleteRequest;
import com.dzy.model.dto.comment.CommentQueryRequest;
import com.dzy.model.dto.comment.ReplyCreateRequest;
import com.dzy.model.entity.Comment;
import com.dzy.model.vo.comment.CommentVO;
import com.dzy.model.vo.userinfo.UserLoginVO;

/**
 * @author DZY
 * @description 针对表【comment(用户评论表)】的数据库操作Service
 * @createDate 2024-04-05 10:40:39
 */
public interface CommentService extends IService<Comment> {

    /**
     * 创建歌曲评论
     *
     * @param commentCreateRequest
     * @param loginUserVO
     * @return
     */
    Boolean createComment(CommentCreateRequest commentCreateRequest, UserLoginVO loginUserVO);

    /**
     * 创建歌曲评论回复
     *
     * @param replyCreateRequest
     * @param loginUserVO
     * @return
     */
    Boolean createReply(ReplyCreateRequest replyCreateRequest, UserLoginVO loginUserVO);

    /**
     * 分页查询自己的评论
     *
     * @param commentQueryRequest
     * @param loginUserVO
     * @return
     */
    Page<CommentVO> listMyCommentByPage(CommentQueryRequest commentQueryRequest, UserLoginVO loginUserVO);

    /**
     * Comment转CommentVO对象
     *
     * @param comment
     * @return
     */
    CommentVO commentToCommentVO(Comment comment);

    /**
     * 删除自己的评论
     *
     * @param commentDeleteRequest
     * @param loginUserVO
     * @return
     */
    Boolean deleteMySongComment(CommentDeleteRequest commentDeleteRequest, UserLoginVO loginUserVO);
}
