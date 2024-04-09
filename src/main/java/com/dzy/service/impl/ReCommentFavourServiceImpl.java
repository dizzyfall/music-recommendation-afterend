package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.ReCommentFavourMapper;
import com.dzy.model.dto.commentfavour.CommentFavourAddRequest;
import com.dzy.model.entity.ReCommentFavour;
import com.dzy.service.CommentService;
import com.dzy.service.ReCommentFavourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DZY
 * @description 针对表【re_comment_favour(评论点赞关联表(硬删除))】的数据库操作Service实现
 * @createDate 2024-04-05 11:25:09
 */
@Service
public class ReCommentFavourServiceImpl extends ServiceImpl<ReCommentFavourMapper, ReCommentFavour>
        implements ReCommentFavourService {

    @Autowired
    private CommentService commentService;

    /**
     * 点赞/取消点赞
     *
     * @param commentFavourAddRequest
     * @return 点赞数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean doCommentFavour(CommentFavourAddRequest commentFavourAddRequest) {
        //校验
        Long commentId = commentFavourAddRequest.getCommentId();
        if (commentId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long userId = commentFavourAddRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //查询数据
        ReCommentFavour reCommentFavour = new ReCommentFavour();
        reCommentFavour.setCommentId(commentId);
        reCommentFavour.setUserId(userId);
        QueryWrapper<ReCommentFavour> queryWrapper = new QueryWrapper<>(reCommentFavour);
        ReCommentFavour oldCommentFavour = this.getOne(queryWrapper);
        boolean result;
        //已点赞
        if (oldCommentFavour != null) {
            //关联表移除数据
            result = this.remove(queryWrapper);
            if (result) {
                //点赞数-1
                result = commentService.update()
                        .eq("id", commentId)
                        .gt("favour_count", 0)
                        .setSql("favour_count = favour_count - 1")
                        .update();
                return result;
            } else {
                throw new BusinessException(StatusCode.SYSTEM_ERROR);
            }
        } else {
            //未点赞
            //关联表添加数据
            result = this.save(reCommentFavour);
            if (result) {
                //点赞数+1
                result = commentService.update()
                        .eq("id", commentId)
                        .setSql("favour_count = favour_count + 1")
                        .update();
                return result;
            } else {
                throw new BusinessException(StatusCode.SYSTEM_ERROR);
            }
        }
    }
}




