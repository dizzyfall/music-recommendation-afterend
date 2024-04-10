package com.dzy.controller;

import com.dzy.common.BaseResponse;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.dto.collect.CollectAlbumRequest;
import com.dzy.model.dto.collect.CollectSongRequest;
import com.dzy.model.vo.userinfo.UserLoginVO;
import com.dzy.service.ReCollectAlbumService;
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

    @Resource
    private ReCollectAlbumService reCollectAlbumService;

    /**
     * 收藏 | 取消收藏 歌曲
     *
     * @param collectSongRequest
     * @param request
     * @return
     */
    @RequestMapping("/song")
    public BaseResponse<Boolean> collectSong(@RequestBody CollectSongRequest collectSongRequest, HttpServletRequest request) {
        if (collectSongRequest == null) {
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
        Long requestUserId = collectSongRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isCollectSongCreate = reCollectSongService.doCollectSong(collectSongRequest);
        if (!isCollectSongCreate) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "收藏歌曲失败");
        }
        return ResponseUtil.success(StatusCode.CREATE_SUCESS, "收藏歌曲成功");
    }

    /**
     * 收藏 | 取消收藏 专辑
     *
     * @param collectAlbumRequest
     * @param request
     * @return
     */
    @RequestMapping("/album")
    public BaseResponse<Boolean> collectAlbum(@RequestBody CollectAlbumRequest collectAlbumRequest, HttpServletRequest request) {
        if (collectAlbumRequest == null) {
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
        Long requestUserId = collectAlbumRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isCollectSongCreate = reCollectAlbumService.doCollectAlbum(collectAlbumRequest);
        if (!isCollectSongCreate) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "收藏专辑失败");
        }
        return ResponseUtil.success(StatusCode.CREATE_SUCESS, "收藏专辑成功");
    }

}
