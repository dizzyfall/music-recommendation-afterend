package com.dzy.controller;

import com.dzy.common.BaseResponse;
import com.dzy.commonutils.ResponseUtil;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.dto.replyfavour.ReplyFavourAddRequest;
import com.dzy.model.vo.userinfo.UserLoginVO;
import com.dzy.service.ReReplyFavourService;
import com.dzy.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @date 2024/5/20  10:46
 */
@RestController
@RequestMapping("/reply_favour")
public class ReplyFavourContorller {

    @Autowired
    private ReReplyFavourService reReplyFavourService;

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 点赞/取消点赞
     *
     * @param replyFavourAddRequest
     * @param request
     * @return 点赞数
     */
    @PostMapping("/")
    public BaseResponse<Boolean> doReplyFavour(@RequestBody ReplyFavourAddRequest replyFavourAddRequest, HttpServletRequest request) {
        if (replyFavourAddRequest == null) {
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
        Long requestUserId = replyFavourAddRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isFavourCount = reReplyFavourService.doReplyFavour(replyFavourAddRequest);
        if (!isFavourCount) {
            throw new BusinessException(StatusCode.UPDATE_ERROR);
        }
        return ResponseUtil.success(StatusCode.UPDATE_SUCESS);
    }

}
