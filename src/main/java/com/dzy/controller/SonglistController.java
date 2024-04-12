package com.dzy.controller;

import com.dzy.common.BaseResponse;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.dto.songlist.AddSongBatchesRequest;
import com.dzy.model.dto.songlist.AddSongRequest;
import com.dzy.model.dto.songlist.SonglistCreateRequest;
import com.dzy.model.vo.userinfo.UserLoginVO;
import com.dzy.service.SonglistService;
import com.dzy.service.UserInfoService;
import com.dzy.utils.ResponseUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/11  23:19
 */
@RestController
@RequestMapping("/songlist")
public class SonglistController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private SonglistService songlistService;

    /**
     * 创建歌单
     *
     * @param songlistCreateRequest
     * @param request
     * @return
     */
    @PostMapping("/create")
    public BaseResponse<Boolean> songlistCreate(@RequestBody SonglistCreateRequest songlistCreateRequest, HttpServletRequest request) {
        if (songlistCreateRequest == null) {
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
        Long requestUserId = songlistCreateRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isSonglistCreate = songlistService.createSonglist(songlistCreateRequest);
        if (!isSonglistCreate) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "创建歌单失败");
        }
        return ResponseUtil.success(StatusCode.CREATE_SUCESS, "创建歌单成功");
    }

    /**
     * 添加歌曲到歌单（单个添加）
     *
     * @param addSongRequest
     * @param request
     * @return
     */
    @PostMapping("/create/single")
    public BaseResponse<Boolean> songSingleAdd(@RequestBody AddSongRequest addSongRequest, HttpServletRequest request) {
        if (addSongRequest == null) {
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
        Long requestUserId = addSongRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isSonglistCreate = songlistService.addSong(addSongRequest);
        if (!isSonglistCreate) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "添加歌曲失败");
        }
        return ResponseUtil.success(StatusCode.CREATE_SUCESS, "添加歌曲成功");
    }

    /**
     * 添加歌曲到歌单（批量添加）
     *
     * @param addSongBatchesRequest
     * @param request
     * @return
     */
    @PostMapping("/create/single")
    public BaseResponse<Boolean> songBatchesAdd(@RequestBody AddSongBatchesRequest addSongBatchesRequest, HttpServletRequest request) {
        if (addSongBatchesRequest == null) {
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
        Long requestUserId = addSongBatchesRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isSonglistCreate = songlistService.addSongBatches(addSongBatchesRequest);
        if (!isSonglistCreate) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "批量添加歌曲失败");
        }
        return ResponseUtil.success(StatusCode.CREATE_SUCESS, "批量添加歌曲成功");
    }

}
