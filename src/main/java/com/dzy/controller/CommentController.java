package com.dzy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dzy.common.BaseResponse;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.dto.comment.CommentQueryRequest;
import com.dzy.model.vo.comment.CommentVO;
import com.dzy.model.vo.userinfo.UserLoginVO;
import com.dzy.service.CommentService;
import com.dzy.service.UserInfoService;
import com.dzy.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
     * 查询自己所有的评论
     *
     * @param commentQueryRequest
     * @param request
     * @return
     */
    @RequestMapping("/my/list/page")
    public BaseResponse<List<CommentVO>> myCommentListRetrieveByPage(@RequestBody CommentQueryRequest commentQueryRequest, HttpServletRequest request){
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
        Long requestId = commentQueryRequest.getUserId();
        if (!loginUserId.equals(requestId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Page<CommentVO> commentVOPage= commentService.listMyCommentByPage(commentQueryRequest, loginUserVO);
        List<CommentVO> commentVOList = commentVOPage.getRecords();
        return ResponseUtil.success(StatusCode.RETRIEVE_SUCCESS,commentVOList,"获取用户评论成功");
    }
}
