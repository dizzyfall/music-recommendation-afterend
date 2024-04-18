package com.dzy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.model.dto.comment.CommentDeleteRequest;
import com.dzy.model.dto.comment.CommentQueryRequest;
import com.dzy.model.entity.Comment;
import com.dzy.model.enums.CommentTypeEum;
import com.dzy.model.vo.comment.CommentVO;

/**
 * @author DZY
 * @description 针对表【comment(用户评论表)】的数据库操作Service
 * @createDate 2024-04-05 10:40:39
 */
public interface CommentService extends IService<Comment> {

    /**
     * 分页查询自己的评论
     *
     * @param commentQueryRequest
     * @return
     */
    Page<CommentVO> listMyCommentByPage(CommentQueryRequest commentQueryRequest);

    /**
     * 获取CommentVO视图对象
     *
     * @param comment
     * @return com.dzy.model.vo.comment.CommentVO
     * @date 2024/4/15  21:36
     */
    CommentVO getCommentVO(Comment comment);

    /**
     * 删除自己的评论
     *
     * @param commentDeleteRequest
     * @return
     */
    Boolean deleteMyComment(CommentDeleteRequest commentDeleteRequest);

    /**
     * 根据评论id获取评论类型
     *
     * @param commentId
     * @return com.dzy.model.enums.CommentTypeEum
     * @date 2024/4/18  12:32
     */
    CommentTypeEum getCommentTypeById(Long commentId);

    /**
     * 根据评论类型删除对应评论表数据
     *
     * @param commentTypeEum
     * @param commentId
     * @return java.lang.Boolean
     * @date 2024/4/18  12:32
     */
    Boolean deleteCommentByCommentTypeEum(CommentTypeEum commentTypeEum, Long commentId);

}
