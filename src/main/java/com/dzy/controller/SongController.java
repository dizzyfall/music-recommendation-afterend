package com.dzy.controller;

import com.dzy.common.BaseResponse;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.dto.song.ReplyCreateRequest;
import com.dzy.model.dto.song.SongCommentCreateRequest;
import com.dzy.model.vo.userinfo.UserLoginVO;
import com.dzy.service.SongService;
import com.dzy.service.UserInfoService;
import com.dzy.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/3  11:52
 */
@RestController
@RequestMapping("/song")
public class SongController {

    @Autowired
    private SongService songService;

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 创建歌曲评论
     *
     * @param songCommentCreateRequest
     * @return
     */
    public BaseResponse<Boolean> songCommentCreate(@RequestBody SongCommentCreateRequest songCommentCreateRequest, HttpServletRequest request) {
        if (songCommentCreateRequest == null) {
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
        Long requestId = songCommentCreateRequest.getCreateUserId();
        if (!loginUserId.equals(requestId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isSongCommentCreate = songService.createComment(songCommentCreateRequest, loginUserVO);
        if (!isSongCommentCreate) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "创建评论失败");
        }
        return ResponseUtil.success(StatusCode.CREATE_SUCESS, "创建评论成功");
    }

    /**
     * 创建歌曲评论
     *
     * @param request
     * @return
     */
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
        Long requestId = replyCreateRequest.getCreateUserId();
        if (!loginUserId.equals(requestId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isSongCommentReply = songService.createReply(replyCreateRequest, loginUserVO);
        if (!isSongCommentReply) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "回复评论失败");
        }
        return ResponseUtil.success(StatusCode.CREATE_SUCESS, "回复评论成功");
    }
}
