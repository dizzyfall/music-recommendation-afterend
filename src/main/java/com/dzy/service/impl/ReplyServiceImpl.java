package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.ReplyMapper;
import com.dzy.model.dto.reply.ReplyCreateRequest;
import com.dzy.model.dto.reply.ReplyQueryRequest;
import com.dzy.model.entity.*;
import com.dzy.model.enums.CommentTypeEum;
import com.dzy.model.vo.reply.ReplyVO;
import com.dzy.model.vo.userinfo.UserInfoIntroVO;
import com.dzy.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author DZY
 * @description 针对表【reply(回复表)】的数据库操作Service实现
 * @createDate 2024-04-15 22:25:45
 */
@Service
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply>
        implements ReplyService {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private CommentService commentService;

    @Resource
    private ReSongCommentService reSongCommentService;

    @Resource
    private ReAlbumCommentService reAlbumCommentService;

    @Resource
    private ReSonglistCommentService reSonglistCommentService;

    /**
     * 获取ReplyVO视图对象
     *
     * @param reply
     * @return com.dzy.model.vo.reply.ReplyVO
     * @date 2024/4/16  15:41
     */
    @Override
    public ReplyVO getReplyVO(Reply reply) {
        if (reply == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //设置Reply中原有的属性
        ReplyVO replyVO = ReplyVO.objToVO(reply);
        //设置Reply额外添加的属性
        //创建回复的用户信息
        UserInfoIntroVO userInfoIntroVO = userInfoService.getUserInfoIntroVOById(reply.getUserId());
        replyVO.setUserInfoIntroVO(userInfoIntroVO);
        //接收回复的用户昵称
        UserInfoIntroVO receiverInfoIntroVO = userInfoService.getUserInfoIntroVOById(reply.getReceiverId());
        replyVO.setReceiverNickName(receiverInfoIntroVO.getNickname());
        return replyVO;
    }

    /**
     * 创建歌曲、专辑、歌单评论回复
     *
     * @param replyCreateRequest
     * @return java.lang.Boolean
     * @date 2024/4/16  23:25
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createReply(ReplyCreateRequest replyCreateRequest) {
        if (replyCreateRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long userId = replyCreateRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //评论是否存在
        Long commentId = replyCreateRequest.getCommentId();
        if (commentId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Comment comment = commentService.getById(commentId);
        if (comment == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "评论不存在");
        }
        String content = replyCreateRequest.getContent();
        if (StringUtils.isBlank(content)) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "内容不能为空");
        }
        Long receiverId = replyCreateRequest.getReceiverId();
        if (receiverId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //获取指定评论的回复用户
        QueryWrapper<Reply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comment_id", commentId);
        List<Reply> replyList = this.list(queryWrapper);
        Set<Long> replyIdSet = replyList.stream().map(Reply::getUserId).collect(Collectors.toSet());
        //添加评论创建用户
        replyIdSet.add(comment.getUserId());
        //判断要回复的人存在存不在这个评论里面
        if (!replyIdSet.contains(receiverId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "回复的用户不存在");
        }
        String commentType = replyCreateRequest.getCommentType();
        if (StringUtils.isBlank(commentType)) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //校验评论类型和评论是否存在且匹配
        validCommentType(commentId, commentType);
        //回复表插入数据
        Reply reply = new Reply();
        reply.setUserId(userId);
        reply.setCommentId(commentId);
        reply.setReceiverId(receiverId);
        reply.setContent(content);
        reply.setPublishTime(new Date());
        reply.setCommentType(commentType);
        boolean isSongReplySave = this.save(reply);
        if (!isSongReplySave) {
            throw new BusinessException(StatusCode.CREATE_ERROR, "创建回复失败");
        }
        return true;
    }

    /**
     * 分页查询歌曲、专辑、歌单指定评论的回复
     *
     * @param replyQueryRequest
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.dzy.model.vo.reply.ReplyVO>
     * @date 2024/4/16  22:57
     */
    @Override
    public Page<ReplyVO> listReplyByPage(ReplyQueryRequest replyQueryRequest) {
        if (replyQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long commentId = replyQueryRequest.getCommentId();
        if (commentId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        QueryWrapper<Reply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comment_id", commentId).orderByAsc("publish_time");
        int pageCurrent = replyQueryRequest.getPageCurrent();
        int pageSize = replyQueryRequest.getPageSize();
        Page<Reply> page = new Page<>(pageCurrent, pageSize);
        Page<Reply> replyPage = this.page(page, queryWrapper);
        List<ReplyVO> replyVOList = replyPage.getRecords().stream().map(this::getReplyVO).collect(Collectors.toList());
        Page<ReplyVO> replyVOPage = new Page<>(pageCurrent, pageSize, replyPage.getTotal());
        replyVOPage.setRecords(replyVOList);
        return replyVOPage;
    }

    /**
     * 校验评论类型和评论是否存在且匹配
     * 默认评论类型一定从前端正确传来的（对某一个评论创建的回复时，评论类型正确，但是评论不一定匹配）
     *
     * @param commentId
     * @param commentType
     * @return void
     * @date 2024/4/17  11:01
     */
    public void validCommentType(Long commentId, String commentType) {
        //获取评论类型的枚举对象
        CommentTypeEum commentTypeEum = CommentTypeEum.getEnumByValue(commentType);
        if (commentTypeEum == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "评论类型请求参数错误");
        }
        //评论类型和评论是否匹配
        QueryWrapper<ReSongComment> songTypeQueryWrapper = new QueryWrapper<>();
        songTypeQueryWrapper.eq("comment_id", commentId);
        if (commentTypeEum.equals(CommentTypeEum.SONG_TYPE)) {
            ReSongComment reSongComment = reSongCommentService.getOne(songTypeQueryWrapper);
            if (reSongComment == null) {
                throw new BusinessException(StatusCode.PARAMS_ERROR, "评论类型和评论不匹配错误，评论类型为"
                        + CommentTypeEum.SONG_TYPE.getCommentType() + "类型"
                        + "，但实际评论不为" + CommentTypeEum.SONG_TYPE.getCommentType() + "评论");
            }
        }
        QueryWrapper<ReAlbumComment> albumTypeQueryWrapper = new QueryWrapper<>();
        albumTypeQueryWrapper.eq("comment_id", commentId);
        if (commentTypeEum.equals(CommentTypeEum.ALBUM_TYPE)) {
            ReAlbumComment reAlbumComment = reAlbumCommentService.getOne(albumTypeQueryWrapper);
            if (reAlbumComment == null) {
                throw new BusinessException(StatusCode.PARAMS_ERROR, "评论类型和评论不匹配错误，评论类型为"
                        + CommentTypeEum.ALBUM_TYPE.getCommentType() + "类型"
                        + "，但实际评论不为" + CommentTypeEum.ALBUM_TYPE.getCommentType() + "评论");
            }
        }
        QueryWrapper<ReSonglistComment> songlistTypeQueryWrapper = new QueryWrapper<>();
        songlistTypeQueryWrapper.eq("comment_id", commentId);
        if (commentTypeEum.equals(CommentTypeEum.SONGLIST_TYPE)) {
            ReSonglistComment reSonglistComment = reSonglistCommentService.getOne(songlistTypeQueryWrapper);
            if (reSonglistComment == null) {
                throw new BusinessException(StatusCode.PARAMS_ERROR, "评论类型和评论不匹配错误，评论类型为"
                        + CommentTypeEum.SONGLIST_TYPE.getCommentType() + "类型"
                        + "，但实际评论不为" + CommentTypeEum.SONGLIST_TYPE.getCommentType() + "评论");
            }
        }
    }

}




