package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.SongMapper;
import com.dzy.model.dto.song.ReplyCreateRequest;
import com.dzy.model.dto.song.SongCommentCreateRequest;
import com.dzy.model.entity.ReSongComment;
import com.dzy.model.entity.Song;
import com.dzy.model.entity.UserComment;
import com.dzy.model.vo.userinfo.UserLoginVO;
import com.dzy.service.ReSongCommentService;
import com.dzy.service.SongService;
import com.dzy.service.UserCommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author DZY
 * @description 针对表【song(歌曲表)】的数据库操作Service实现
 * @createDate 2024-04-03 11:40:52
 */
@Service
public class SongServiceImpl extends ServiceImpl<SongMapper, Song>
        implements SongService {

    @Autowired
    private ReSongCommentService reSongCommentService;

    @Autowired
    private UserCommentService userCommentService;

    /**
     * 创建歌曲评论
     *
     * @param songCommentCreateRequest
     * @param loginUserVO
     * @return
     */
    //todo 是否和回复评论写一起
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createComment(SongCommentCreateRequest songCommentCreateRequest, UserLoginVO loginUserVO) {
        //todo 判断登录信息是否一致
        //校验
        Long songId = songCommentCreateRequest.getSongId();
        if (songId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long createUserId = songCommentCreateRequest.getCreateUserId();
        if (createUserId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        String content = songCommentCreateRequest.getContent();
        if (StringUtils.isBlank(content)) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "内容不能为空");
        }
        UserComment comment = new UserComment();
        comment.setUserId(createUserId);
        comment.setContent(content);
        comment.setFavourCount(0L);
        comment.setPublishTime(new Date());
        boolean isUserCommentSave = userCommentService.save(comment);
        if (!isUserCommentSave) {
            throw new BusinessException(StatusCode.CREATE_ERROR, "创建评论失败");
        }
        //获取插入用户评论表的主键id
        Long userCmtId = comment.getId();
        //维护关联表
        ReSongComment reSongComment = new ReSongComment();
        reSongComment.setSongId(songId);
        reSongComment.setUserCmtId(userCmtId);
        reSongComment.setCreateUserId(createUserId);
        //followerId为-1,replyUserId为-1
        reSongComment.setFollowerId(-1L);
        reSongComment.setReplyUserId(-1L);
        boolean isReSongCommentSave = reSongCommentService.save(reSongComment);
        if (!isReSongCommentSave) {
            throw new BusinessException(StatusCode.CREATE_ERROR, "创建评论失败");
        }
        return true;
    }


    /**
     * 创建歌曲评论回复
     *
     * @param replyCreateRequest
     * @param loginUserVO
     * @return
     */
    //todo 是否和回复评论写一起
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createReply(ReplyCreateRequest replyCreateRequest, UserLoginVO loginUserVO) {
        //todo 判断登录信息是否一致
        //校验
        Long songId = replyCreateRequest.getSongId();
        if (songId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long userCmtId = replyCreateRequest.getUserCmtId();
        if (userCmtId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long createUserId = replyCreateRequest.getCreateUserId();
        if (createUserId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        String content = replyCreateRequest.getContent();
        if (StringUtils.isBlank(content)) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "内容不能为空");
        }
        Long followerId = replyCreateRequest.getFollowerId();
        if (followerId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long replyUserId = replyCreateRequest.getReplyUserId();
        if (replyUserId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        UserComment comment = new UserComment();
        comment.setUserId(createUserId);
        comment.setContent(content);
        comment.setFavourCount(0L);
        comment.setPublishTime(new Date());
        boolean isUserCommentSave = userCommentService.save(comment);
        if (!isUserCommentSave) {
            throw new BusinessException(StatusCode.CREATE_ERROR, "创建评论失败");
        }
        Long newUserCmtId = comment.getId();
        QueryWrapper<ReSongComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_cmt_id", userCmtId);
        ReSongComment oldReSongComment = reSongCommentService.getOne(queryWrapper);
        ReSongComment newReSongComment = new ReSongComment();
        //todo 这里的songId和通过userCmtId查询的数据的songId是否一样,同理其他字段
        newReSongComment.setSongId(songId);
        newReSongComment.setUserCmtId(newUserCmtId);
        newReSongComment.setCreateUserId(oldReSongComment.getCreateUserId());
        newReSongComment.setFollowerId(followerId);
        newReSongComment.setReplyUserId(createUserId);
        boolean isReSongCommentSave = reSongCommentService.save(newReSongComment);
        if (!isReSongCommentSave) {
            throw new BusinessException(StatusCode.CREATE_ERROR, "创建评论失败");
        }
        return true;
    }
}




