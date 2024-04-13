package com.dzy.controller;

import com.dzy.common.BaseResponse;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.dto.songlist.*;
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
    @PostMapping("/add_song")
    public BaseResponse<Boolean> songAdd(@RequestBody AddSongRequest addSongRequest, HttpServletRequest request) {
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
        Boolean isSongAdd = songlistService.addSong(addSongRequest);
        if (!isSongAdd) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "添加歌曲失败");
        }
        return ResponseUtil.success(StatusCode.CREATE_SUCESS, "添加歌曲成功");
    }

    /**
     * 添加歌曲到歌单（批量添加）
     *
     * @param addBatchSongRequest
     * @param request
     * @return
     */
    @PostMapping("/add_song_batch")
    public BaseResponse<Boolean> songBatchAdd(@RequestBody AddBatchSongRequest addBatchSongRequest, HttpServletRequest request) {
        if (addBatchSongRequest == null) {
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
        Long requestUserId = addBatchSongRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isSongBatchAdd = songlistService.addBatchSong(addBatchSongRequest);
        if (!isSongBatchAdd) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "批量添加歌曲失败");
        }
        return ResponseUtil.success(StatusCode.CREATE_SUCESS, "批量添加歌曲成功");
    }

    /**
     * 删除歌单
     *
     * @param songlistDeleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> songlistDelete(@RequestBody SonglistDeleteRequest songlistDeleteRequest, HttpServletRequest request) {
        if (songlistDeleteRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "删除请求参数为空");
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
        Long requestUserId = songlistDeleteRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isSonglistDelete = songlistService.deleteSonglist(songlistDeleteRequest);
        if (!isSonglistDelete) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "删除歌单失败");
        }
        return ResponseUtil.success(StatusCode.CREATE_SUCESS, "删除歌单成功");
    }

    /**
     * 删除歌单（批量删除）
     *
     * @param songlistDeleteBatchRequest
     * @param request
     * @return
     */
    @PostMapping("/delete_batch")
    public BaseResponse<Boolean> songlistBatchDelete(@RequestBody SonglistDeleteBatchRequest songlistDeleteBatchRequest, HttpServletRequest request) {
        if (songlistDeleteBatchRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "删除请求参数为空");
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
        Long requestUserId = songlistDeleteBatchRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isSonglistBatchDelete = songlistService.deleteBatchSonglist(songlistDeleteBatchRequest);
        if (!isSonglistBatchDelete) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "批量删除歌单失败");
        }
        return ResponseUtil.success(StatusCode.CREATE_SUCESS, "批量删除歌单成功");
    }

    /**
     * 删除歌曲
     *
     * @param deleteSongRequest
     * @param request
     * @return
     */
    @PostMapping("/delete_song")
    public BaseResponse<Boolean> songDelete(@RequestBody DeleteSongRequest deleteSongRequest, HttpServletRequest request) {
        if (deleteSongRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "删除请求参数为空");
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
        Long requestUserId = deleteSongRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isSongDelete = songlistService.deleteSong(deleteSongRequest);
        if (!isSongDelete) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "删除歌曲失败");
        }
        return ResponseUtil.success(StatusCode.CREATE_SUCESS, "删除歌曲成功");
    }

    /**
     * 删除歌曲（批量删除）
     *
     * @param deleteBatchSongRequest
     * @param request
     * @return
     */
    @PostMapping("/delete_song_batch")
    public BaseResponse<Boolean> songBatchDelete(@RequestBody DeleteBatchSongRequest deleteBatchSongRequest, HttpServletRequest request) {
        if (deleteBatchSongRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "删除请求参数为空");
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
        Long requestUserId = deleteBatchSongRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isSongBatchDelete = songlistService.deleteBatchSong(deleteBatchSongRequest);
        if (!isSongBatchDelete) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "批量删除歌曲失败");
        }
        return ResponseUtil.success(StatusCode.CREATE_SUCESS, "批量删除歌曲成功");
    }

}
