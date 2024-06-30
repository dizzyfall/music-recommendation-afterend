package com.dzy.controller;

import com.dzy.common.BaseResponse;
import com.dzy.commonutils.ResponseUtil;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.dto.userrating.SongRatingCreateRequest;
import com.dzy.model.entity.UserRating;
import com.dzy.model.vo.userinfo.UserLoginVO;
import com.dzy.service.UserInfoService;
import com.dzy.service.UserRatingService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/5/20  23:09
 */
@RestController
@RequestMapping("/rating")
public class UserRatingController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserRatingService userRatingService;

    /**
     * 创建歌曲评分
     *
     * @param songRatingCreateRequest
     * @param request
     * @return
     */
    @PostMapping("/song/create")
    public BaseResponse<Boolean> SongRatingCreate(@RequestBody SongRatingCreateRequest songRatingCreateRequest, HttpServletRequest request) {
        if (songRatingCreateRequest == null) {
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
        Long requestUserId = songRatingCreateRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isSongRatingCreate = userRatingService.createSongRating(songRatingCreateRequest);
        if (!isSongRatingCreate) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "创建歌曲评分失败");
        }
        return ResponseUtil.success(StatusCode.CREATE_SUCESS, "创建歌曲评分成功");
    }

    /**
     * 获取歌曲评分
     *
     * @return
     */
    @GetMapping("/rating/list")
    public BaseResponse<List<UserRating>> getSongRating() {
        List<UserRating> userRatingList = userRatingService.getUserRatingList();
        return ResponseUtil.success(StatusCode.RETRIEVE_SUCCESS, userRatingList, "查询评分列表成功");
    }

}
