package com.dzy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dzy.common.BaseResponse;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.dto.reply.MyReplyDeleteRequest;
import com.dzy.model.dto.reply.MyReplyQueryRequest;
import com.dzy.model.vo.reply.ReplyVO;
import com.dzy.model.vo.userinfo.UserLoginVO;
import com.dzy.service.ReplyService;
import com.dzy.service.UserInfoService;
import com.dzy.commonutils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/18  10:43
 */
@RestController
@RequestMapping("/reply")
public class ReplyController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ReplyService replyService;

    /**
     * 分页查询自己的回复
     *
     * @param myReplyQueryRequest
     * @param request
     * @return com.dzy.common.BaseResponse<java.util.List < com.dzy.model.vo.comment.CommentVO>>
     * @date 2024/4/18  10:44
     */
    @PostMapping("/my/list/page")
    public BaseResponse<List<ReplyVO>> myReplyListRetrieveByPage(@RequestBody MyReplyQueryRequest myReplyQueryRequest, HttpServletRequest request) {
        if (myReplyQueryRequest == null) {
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
        Long requestUserId = myReplyQueryRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Page<ReplyVO> replyVOPage = replyService.listMyReplyByPage(myReplyQueryRequest);
        List<ReplyVO> replyVOList = replyVOPage.getRecords();
        return ResponseUtil.success(StatusCode.RETRIEVE_SUCCESS, replyVOList, "获取用户回复成功");
    }

    /**
     * 删除自己的回复
     *
     * @param myReplyDeleteRequest
     * @param request
     * @return com.dzy.common.BaseResponse<java.lang.Boolean>
     * @date 2024/4/18  10:44
     */
    @PostMapping("/my/delete")
    public BaseResponse<Boolean> myReplyDelete(@RequestBody MyReplyDeleteRequest myReplyDeleteRequest, HttpServletRequest request) {
        if (myReplyDeleteRequest == null) {
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
        Long requestUserId = myReplyDeleteRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isDeleteMyReply = replyService.deleteMyReply(myReplyDeleteRequest);
        if (!isDeleteMyReply) {
            throw new BusinessException(StatusCode.DELETE_ERROR);
        }
        return ResponseUtil.success(StatusCode.DELETE_SUCCESS, "删除回复成功");
    }

}
