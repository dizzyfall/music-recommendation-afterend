package com.dzy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dzy.common.BaseResponse;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.dto.song.SongCommentQueryRequest;
import com.dzy.model.vo.comment.CommentVO;
import com.dzy.service.SongService;
import com.dzy.service.UserInfoService;
import com.dzy.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
