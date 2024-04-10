package com.dzy.controller;

import com.dzy.common.BaseResponse;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.dto.collect.CollectSongCreateRequest;
import com.dzy.model.vo.userinfo.UserLoginVO;
import com.dzy.service.ReCollectSongService;
import com.dzy.service.UserInfoService;
import com.dzy.utils.ResponseUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/9  23:31
 */
@RestController
@RequestMapping("/collect")
public class CollectController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private ReCollectSongService reCollectSongService;

    /**
     * 收藏歌曲
     *
     * @param collectSongCreateRequest
     * @param request
     * @return
     */
    public BaseResponse<Boolean> collectSongCreate(@RequestBody CollectSongCreateRequest collectSongCreateRequest, HttpServletRequest request) {
        if (collectSongCreateRequest == null) {
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
        Long requestUserId = collectSongCreateRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isCollectSongCreate = reCollectSongService.createCollectSong(collectSongCreateRequest);
        if (!isCollectSongCreate) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "收藏歌曲失败");
        }
        return ResponseUtil.success(StatusCode.CREATE_SUCESS, "收藏歌曲成功");
    }

}
