package com.dzy.controller;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/7  13:35
 */

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dzy.common.BaseResponse;
import com.dzy.commonutils.ResponseUtil;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.dto.album.AlbumCommentCreateRequest;
import com.dzy.model.dto.album.AlbumCommentQueryRequest;
import com.dzy.model.dto.album.AlbumQueryRequest;
import com.dzy.model.dto.album.AlbumSongQueryRequest;
import com.dzy.model.dto.reply.ReplyCreateRequest;
import com.dzy.model.dto.reply.ReplyQueryRequest;
import com.dzy.model.vo.album.AlbumDetailVO;
import com.dzy.model.vo.album.AlbumIntroVO;
import com.dzy.model.vo.comment.CommentVO;
import com.dzy.model.vo.reply.ReplyVO;
import com.dzy.model.vo.song.SongIntroVO;
import com.dzy.model.vo.userinfo.UserLoginVO;
import com.dzy.service.AlbumService;
import com.dzy.service.ReplyService;
import com.dzy.service.UserInfoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/album")
public class AlbumController {

    @Resource
    private AlbumService albumService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private ReplyService replyService;

    /**
     * 分页查询歌手专辑
     *
     * @param albumQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<List<AlbumIntroVO>> albumRetrieveByPage(@RequestBody AlbumQueryRequest albumQueryRequest) {
        if (albumQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Page<AlbumIntroVO> albumVOPage = albumService.listAlbumByPage(albumQueryRequest);
        List<AlbumIntroVO> albumIntroVOList = albumVOPage.getRecords();
        return ResponseUtil.success(StatusCode.RETRIEVE_SUCCESS, albumIntroVOList);
    }

    /**
     * 查询指定专辑信息
     *
     * @param albumSongQueryRequest
     * @return
     */
    @PostMapping("/info")
    public BaseResponse<AlbumDetailVO> albumInfoRetrieve(@RequestBody AlbumSongQueryRequest albumSongQueryRequest) {
        if (albumSongQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        AlbumDetailVO albumDetailVO = albumService.searchAlbumDetail(albumSongQueryRequest);
        return ResponseUtil.success(StatusCode.RETRIEVE_SUCCESS, albumDetailVO);
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
    public BaseResponse<Boolean> albumCommentCreate(@RequestBody AlbumCommentCreateRequest albumCommentCreateRequest, HttpServletRequest request) {
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

    /**
     * 分页查询专辑的评论
     *
     * @param albumCommentQueryRequest
     * @return com.dzy.common.BaseResponse<java.util.List < com.dzy.model.vo.comment.CommentVO>>
     * @date 2024/4/15  9:35
     */
    @PostMapping("/comment/list/page")
    public BaseResponse<List<CommentVO>> albumCommentListRetrieveByPage(@RequestBody AlbumCommentQueryRequest albumCommentQueryRequest) {
        if (albumCommentQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Page<CommentVO> albumCommentVOPage = albumService.listAlbumCommentByPage(albumCommentQueryRequest);
        List<CommentVO> albumCommentVOList = albumCommentVOPage.getRecords();
        return ResponseUtil.success(StatusCode.RETRIEVE_SUCCESS, albumCommentVOList, "获取专辑评论成功");
    }

    /**
     * 创建专辑评论回复
     *
     * @param replyCreateRequest
     * @param request
     * @return com.dzy.common.BaseResponse<java.lang.Boolean>
     * @date 2024/4/16  21:42
     */
    @PostMapping("/reply/create")
    public BaseResponse<Boolean> albumReplyCreate(@RequestBody ReplyCreateRequest replyCreateRequest, HttpServletRequest request) {
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
        Long requestUserId = replyCreateRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isAlbumReplyCreate = replyService.createReply(replyCreateRequest);
        if (!isAlbumReplyCreate) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "创建回复失败");
        }
        return ResponseUtil.success(StatusCode.CREATE_SUCESS, "创建回复成功");
    }

    /**
     * 分页查询歌曲指定评论的回复
     *
     * @param replyQueryRequest
     * @return com.dzy.common.BaseResponse<java.util.List < com.dzy.model.vo.reply.ReplyVO>>
     * @date 2024/4/16  22:33
     */
    @PostMapping("/reply/list/page")
    public BaseResponse<List<ReplyVO>> ablumReplyListRetrieveByPage(@RequestBody ReplyQueryRequest replyQueryRequest) {
        if (replyQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Page<ReplyVO> albumReplyVOPage = replyService.listReplyByPage(replyQueryRequest);
        List<ReplyVO> albumReplyVOList = albumReplyVOPage.getRecords();
        return ResponseUtil.success(StatusCode.RETRIEVE_SUCCESS, albumReplyVOList, "获取专辑评论的回复列表成功");
    }

}
