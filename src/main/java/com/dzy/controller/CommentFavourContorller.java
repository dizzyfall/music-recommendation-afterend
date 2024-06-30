package com.dzy.controller;

import com.dzy.common.BaseResponse;
import com.dzy.commonutils.ResponseUtil;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.dto.commentfavour.CommentFavourAddRequest;
import com.dzy.model.vo.userinfo.UserLoginVO;
import com.dzy.service.ReCommentFavourService;
import com.dzy.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/3  17:41
 */
@RestController
@RequestMapping("/comment_favour")
public class CommentFavourContorller {

    @Autowired
    private ReCommentFavourService reCommentFavourService;

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 点赞/取消点赞
     *
     * @param commentFavourAddRequest
     * @param request
     * @return 点赞数
     */
    @PostMapping("/")
    public BaseResponse<Boolean> doCommentFavour(@RequestBody CommentFavourAddRequest commentFavourAddRequest, HttpServletRequest request) {
        if (commentFavourAddRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "更新请求参数为空");
        }
        if (request == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        UserLoginVO loginUserVO = userInfoService.getUserInfoLoginState(request);
        if (loginUserVO == null) {
            throw new BusinessException(StatusCode.NO_LOGIN_ERROR);
        }
        Long loginUserId = loginUserVO.getId();
        Long requestUserId = commentFavourAddRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isFavourCount = reCommentFavourService.doCommentFavour(commentFavourAddRequest);
        if (!isFavourCount) {
            throw new BusinessException(StatusCode.UPDATE_ERROR);
        }
        return ResponseUtil.success(StatusCode.UPDATE_SUCESS);
    }

}
