package com.dzy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dzy.common.BaseResponse;
import com.dzy.commonutils.ResponseUtil;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.dto.collect.CollectAlbumRequest;
import com.dzy.model.dto.collect.CollectQueryRequest;
import com.dzy.model.dto.collect.CollectSongRequest;
import com.dzy.model.dto.collect.CollectSonglistRequest;
import com.dzy.model.vo.collect.CollectAlbumVO;
import com.dzy.model.vo.collect.CollectCountVO;
import com.dzy.model.vo.song.SongIntroVO;
import com.dzy.model.vo.songlist.SonglistIntroVO;
import com.dzy.model.vo.userinfo.UserLoginVO;
import com.dzy.service.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @Resource
    private CollectService collectService;

    @Resource
    private ReCollectSonglistService reCollectSonglistService;

    /**
     * 收藏 | 取消收藏 歌曲
     *
     * @param collectSongRequest
     * @param request
     * @return
     */
    @PostMapping("/song")
    public BaseResponse<Boolean> collectSong(@RequestBody CollectSongRequest collectSongRequest, HttpServletRequest request) {
        if (collectSongRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
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
        Boolean isCollectSong = reCollectSongService.doCollectSong(collectSongRequest);
        if (!isCollectSong) {
            throw new BusinessException(StatusCode.CREATE_ERROR, "收藏歌曲失败");
        }
        return ResponseUtil.success(StatusCode.CREATE_SUCESS, "收藏或取消收藏歌曲成功");
    }

    /**
     * 收藏 | 取消收藏 专辑
     *
     * @param collectAlbumRequest
     * @param request
     * @return
     */
    @PostMapping("/album")
    public BaseResponse<Boolean> collectAlbum(@RequestBody CollectAlbumRequest collectAlbumRequest, HttpServletRequest request) {
        if (collectAlbumRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
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
        Boolean isCollectAlbum = reCollectAlbumService.doCollectAlbum(collectAlbumRequest);
        if (!isCollectAlbum) {
            throw new BusinessException(StatusCode.CREATE_ERROR, "收藏专辑失败");
        }
        return ResponseUtil.success(StatusCode.CREATE_SUCESS, "收藏专辑成功");
    }

    /**
     * 收藏 | 取消收藏 歌单
     *
     * @param collectSonglistRequest
     * @param request
     * @return com.dzy.common.BaseResponse<java.lang.Boolean>
     * @date 2024/4/14  9:04
     */
    @PostMapping("/songlist")
    public BaseResponse<Boolean> collectSonglist(@RequestBody CollectSonglistRequest collectSonglistRequest, HttpServletRequest request) {
        if (collectSonglistRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
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
        Long requestUserId = collectSonglistRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isCollectSonglist = reCollectSonglistService.doCollectSonglist(collectSonglistRequest);
        if (!isCollectSonglist) {
            throw new BusinessException(StatusCode.CREATE_ERROR, "收藏歌单失败");
        }
        return ResponseUtil.success(StatusCode.CREATE_SUCESS, "收藏歌单成功");
    }

    /**
     * 获取收藏的歌曲、专辑、歌单数量
     *
     * @param collectQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/count")
    public BaseResponse<CollectCountVO> collectCountRetrieve(@RequestBody CollectQueryRequest collectQueryRequest, HttpServletRequest request) {
        if (collectQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
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
        Long requestUserId = collectQueryRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        CollectCountVO collectCountVO = collectService.getCollectCount(collectQueryRequest);
        return ResponseUtil.success(StatusCode.RETRIEVE_SUCCESS, collectCountVO, "获取收藏数量信息成功");
    }

    /**
     * 分页查询收藏的歌曲
     *
     * @param collectQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/song/list/page")
    public BaseResponse<List<SongIntroVO>> collectSongRetrieveByPage(@RequestBody CollectQueryRequest collectQueryRequest, HttpServletRequest request) {
        if (collectQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
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
        Long requestUserId = collectQueryRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Page<SongIntroVO> songIntroVOPage = collectService.listCollectSongByPage(collectQueryRequest);
        List<SongIntroVO> songIntroVOList = songIntroVOPage.getRecords();
        if (CollectionUtils.isEmpty(songIntroVOList)) {
            throw new BusinessException(StatusCode.DATAS_NULL_ERROR, "暂无歌曲");
        }
        return ResponseUtil.success(StatusCode.RETRIEVE_SUCCESS, songIntroVOList, "获取收藏歌曲列表成功");
    }

    /**
     * 分页查询收藏的专辑
     *
     * @param collectQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/album/list/page")
    public BaseResponse<List<CollectAlbumVO>> collectAlbumRetrieveByPage(@RequestBody CollectQueryRequest collectQueryRequest, HttpServletRequest request) {
        if (collectQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
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
        Long requestUserId = collectQueryRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Page<CollectAlbumVO> albumVOPage = collectService.listCollectAlbumByPage(collectQueryRequest);
        List<CollectAlbumVO> albumVOList = albumVOPage.getRecords();
        if (CollectionUtils.isEmpty(albumVOList)) {
            throw new BusinessException(StatusCode.DATAS_NULL_ERROR, "暂无专辑");
        }
        return ResponseUtil.success(StatusCode.RETRIEVE_SUCCESS, albumVOList, "获取收藏专辑列表成功");
    }

    /**
     * 分页查询收藏的歌单
     *
     * @param collectQueryRequest
     * @param request
     * @return com.dzy.common.BaseResponse<java.util.List < com.dzy.model.vo.collect.CollectAlbumVO>>
     * @date 2024/4/14  10:18
     */
    @PostMapping("/songlist/list/page")
    public BaseResponse<List<SonglistIntroVO>> collectSonglistRetrieveByPage(@RequestBody CollectQueryRequest collectQueryRequest, HttpServletRequest request) {
        if (collectQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
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
        Long requestUserId = collectQueryRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Page<SonglistIntroVO> songlistIntroVOPage = collectService.listCollectSonglistByPage(collectQueryRequest);
        List<SonglistIntroVO> songlistIntroVOList = songlistIntroVOPage.getRecords();
        if (CollectionUtils.isEmpty(songlistIntroVOList)) {
            throw new BusinessException(StatusCode.DATAS_NULL_ERROR, "暂无歌单");
        }
        return ResponseUtil.success(StatusCode.RETRIEVE_SUCCESS, songlistIntroVOList, "获取收藏歌单列表成功");
    }

}
