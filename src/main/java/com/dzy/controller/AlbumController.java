package com.dzy.controller;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/7  13:35
 */

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dzy.common.BaseResponse;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.dto.album.AlbumCommentCreateRequest;
import com.dzy.model.dto.album.AlbumQueryRequest;
import com.dzy.model.dto.album.AlbumSongQueryRequest;
import com.dzy.model.vo.album.AlbumInfoVO;
import com.dzy.model.vo.album.AlbumVO;
import com.dzy.model.vo.song.SongIntroVO;
import com.dzy.model.vo.userinfo.UserLoginVO;
import com.dzy.service.AlbumService;
import com.dzy.service.UserInfoService;
import com.dzy.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/album")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 分页查询歌手专辑
     *
     * @param albumQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<List<AlbumVO>> albumRetrieveByPage(@RequestBody AlbumQueryRequest albumQueryRequest) {
        if (albumQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Page<AlbumVO> albumVOPage = albumService.listAlbumByPage(albumQueryRequest);
        List<AlbumVO> albumVOList = albumVOPage.getRecords();
        return ResponseUtil.success(StatusCode.RETRIEVE_SUCCESS, albumVOList);
    }

    /**
     * 查询指定专辑信息
     *
     * @param albumSongQueryRequest
     * @return
     */
    @PostMapping("/info")
    public BaseResponse<AlbumInfoVO> albumInfoRetrieve(@RequestBody AlbumSongQueryRequest albumSongQueryRequest) {
        if (albumSongQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        AlbumInfoVO albumInfoVO = albumService.searchAlbumInfo(albumSongQueryRequest);
        return ResponseUtil.success(StatusCode.RETRIEVE_SUCCESS, albumInfoVO);
    }

    /**
     * 查询指定专辑所有歌曲
     *
     * @param albumSongQueryRequest
     * @return
     */
    @PostMapping("/song")
    public BaseResponse<List<SongIntroVO>> albumSongRetrieve(@RequestBody AlbumSongQueryRequest albumSongQueryRequest) {
        if (albumSongQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        List<SongIntroVO> songIntroVOList = albumService.listSong(albumSongQueryRequest);
        return ResponseUtil.success(StatusCode.RETRIEVE_SUCCESS, songIntroVOList);
    }

    /**
     * 创建专辑评论
     *
     * @param albumCommentCreateRequest
     * @param request
     * @return
     */
    @PostMapping("/comment/create")
    public BaseResponse<Boolean> songCommentCreate(@RequestBody AlbumCommentCreateRequest albumCommentCreateRequest, HttpServletRequest request) {
        if (albumCommentCreateRequest == null) {
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
        Long requestUserId = albumCommentCreateRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isAlbumCommentCreate = albumService.createComment(albumCommentCreateRequest);
        if (!isAlbumCommentCreate) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "创建评论失败");
        }
        return ResponseUtil.success(StatusCode.CREATE_SUCESS, "创建评论成功");
    }
}
