package com.dzy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dzy.common.BaseResponse;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.dto.comment.CommentCreateRequest;
import com.dzy.model.dto.comment.CommentDeleteRequest;
import com.dzy.model.dto.comment.CommentQueryRequest;
import com.dzy.model.dto.comment.ReplyCreateRequest;
import com.dzy.model.vo.comment.CommentVO;
import com.dzy.model.vo.userinfo.UserLoginVO;
import com.dzy.service.CommentService;
import com.dzy.service.UserInfoService;
import com.dzy.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/5  9:53
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private CommentService commentService;

    /**
     * 创建歌曲评论
     *
     * @param commentCreateRequest
     * @return
     */
    @PostMapping("/create")
    public BaseResponse<Boolean> songCommentCreate(@RequestBody CommentCreateRequest commentCreateRequest, HttpServletRequest request) {
        if (commentCreateRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "创建请求参数为空");
        }
        if (request == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //是否登录
        UserLoginVO loginUserVO = userInfoService.getUserInfoLoginState(request);
        if (loginUserVO == null) {
            throw new BusinessException(StatusCode.NO_LOGIN_ERROR);
        }
        //是否是本人
        Long loginUserId = loginUserVO.getId();
        Long requestUserId = commentCreateRequest.getCreateUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isSongCommentCreate = commentService.createComment(commentCreateRequest, loginUserVO);
        if (!isSongCommentCreate) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "创建评论失败");
        }
        return ResponseUtil.success(StatusCode.CREATE_SUCESS, "创建评论成功");
    }

    /**
     * 创建歌曲评论回复
     *
     * @param request
     * @return
     */
    @PostMapping("/reply")
    public BaseResponse<Boolean> songCommentReply(@RequestBody ReplyCreateRequest replyCreateRequest, HttpServletRequest request) {
        if (replyCreateRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "创建请求参数为空");
        }
        if (request == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //是否登录
        UserLoginVO loginUserVO = userInfoService.getUserInfoLoginState(request);
        if (loginUserVO == null) {
            throw new BusinessException(StatusCode.NO_LOGIN_ERROR);
        }
        //是否是本人
        Long loginUserId = loginUserVO.getId();
        Long requestUserId = replyCreateRequest.getCreateUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isSongCommentReply = commentService.createReply(replyCreateRequest, loginUserVO);
        if (!isSongCommentReply) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "回复评论失败");
        }
        return ResponseUtil.success(StatusCode.CREATE_SUCESS, "回复评论成功");
    }

    /**
     * 分页查询自己的评论
     *
     * @param commentQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page")
    public BaseResponse<List<CommentVO>> myCommentListRetrieveByPage(@RequestBody CommentQueryRequest commentQueryRequest, HttpServletRequest request) {
        if (commentQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "查询请求参数为空");
        }
        if (request == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        UserLoginVO loginUserVO = userInfoService.getUserInfoLoginState(request);
        if (loginUserVO == null) {
            throw new BusinessException(StatusCode.NO_LOGIN_ERROR);
        }
        Long loginUserId = loginUserVO.getId();
        Long requestUserId = commentQueryRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Page<CommentVO> commentVOPage = commentService.listMyCommentByPage(commentQueryRequest, loginUserVO);
        List<CommentVO> commentVOList = commentVOPage.getRecords();
        return ResponseUtil.success(StatusCode.RETRIEVE_SUCCESS, commentVOList, "获取用户评论成功");
    }

    /**
     * 删除自己的评论
     *
     * @param commentDeleteRequest
     * @param request
     * @return
     */
    @PostMapping("/my/comment/delete")
    public BaseResponse<Boolean> songCommentDelete(@RequestBody CommentDeleteRequest commentDeleteRequest, HttpServletRequest request) {
        if (commentDeleteRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "删除请求参数为空");
        }
        if (request == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        UserLoginVO loginUserVO = userInfoService.getUserInfoLoginState(request);
        if (loginUserVO == null) {
            throw new BusinessException(StatusCode.NO_LOGIN_ERROR);
        }
        Long loginUserId = loginUserVO.getId();
        Long requestUserId = commentDeleteRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isDeleteMySongComment = commentService.deleteMySongComment(commentDeleteRequest, loginUserVO);
        if (!isDeleteMySongComment) {
            throw new BusinessException(StatusCode.DELETE_ERROR);
        }
        return ResponseUtil.success(StatusCode.DELETE_SUCCESS, "删除评论成功");
    }
}
