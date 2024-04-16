package com.dzy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dzy.common.BaseResponse;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.dto.song.SongCommentCreateRequest;
import com.dzy.model.dto.song.SongCommentQueryRequest;
import com.dzy.model.dto.song.SongReplyCreateRequest;
import com.dzy.model.dto.song.SongReplyQueryRequest;
import com.dzy.model.vo.comment.CommentVO;
import com.dzy.model.vo.reply.ReplyVO;
import com.dzy.model.vo.userinfo.UserLoginVO;
import com.dzy.service.SongService;
import com.dzy.service.UserInfoService;
import com.dzy.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/3  11:52
 */
@RestController
@RequestMapping("/song")
public class SongController {

    @Autowired
    private SongService songService;

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 创建歌曲评论
     *
     * @param songCommentCreateRequest
     * @return
     */
    @PostMapping("/comment/create")
    public BaseResponse<Boolean> songCommentCreate(@RequestBody SongCommentCreateRequest songCommentCreateRequest, HttpServletRequest request) {
        if (songCommentCreateRequest == null) {
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
        Long requestUserId = songCommentCreateRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isSongCommentCreate = songService.createComment(songCommentCreateRequest);
        if (!isSongCommentCreate) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "创建评论失败");
        }
        return ResponseUtil.success(StatusCode.CREATE_SUCESS, "创建评论成功");
    }

    /**
     * 创建歌曲评论回复
     *
     * @param request
     * @return
     */
    @PostMapping("/comment/reply")
    public BaseResponse<Boolean> songCommentReply(@RequestBody SongReplyCreateRequest songReplyCreateRequest, HttpServletRequest request) {
        if (songReplyCreateRequest == null) {
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
        Long requestUserId = songReplyCreateRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isSongCommentReply = songService.createReply(songReplyCreateRequest);
        if (!isSongCommentReply) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "创建回复评论失败");
        }
        return ResponseUtil.success(StatusCode.CREATE_SUCESS, "创建回复评论成功");
    }

    /**
     * 分页查询歌曲的评论
     *
     * @param songCommentQueryRequest
     * @return
     */
    @PostMapping("/comment/list/page")
    public BaseResponse<List<CommentVO>> songCommentListRetrieveByPage(@RequestBody SongCommentQueryRequest songCommentQueryRequest) {
        if (songCommentQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Page<CommentVO> songCommentVOPage = songService.listSongCommentByPage(songCommentQueryRequest);
        List<CommentVO> songCommentVOList = songCommentVOPage.getRecords();
        return ResponseUtil.success(StatusCode.RETRIEVE_SUCCESS, songCommentVOList, "获取歌曲评论成功");
    }

    /**
     * 分页查询歌曲指定评论的回复
     *
     * @date 2024/4/16  14:58
     * @param songReplyQueryRequest
     * @return com.dzy.common.BaseResponse<java.util.List<com.dzy.model.vo.reply.ReplyVO>>
     */
    @PostMapping("/reply/list/page")
    public BaseResponse<List<ReplyVO>> songReplyListRetrieveByPage(@RequestBody SongReplyQueryRequest songReplyQueryRequest) {
        if (songReplyQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Page<ReplyVO> songReplyVOPage = songService.listSongReplyByPage(songReplyQueryRequest);
        List<ReplyVO> songReplyVOList = songReplyVOPage.getRecords();
        return ResponseUtil.success(StatusCode.RETRIEVE_SUCCESS, songReplyVOList, "获取歌曲评论的回复列表成功");
    }

}
