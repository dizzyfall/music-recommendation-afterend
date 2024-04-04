package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.ReCommentFavourMapper;
import com.dzy.model.dto.commentfavour.CommentFavourAddRequest;
import com.dzy.model.entity.ReCommentFavour;
import com.dzy.model.vo.userinfo.UserLoginVO;
import com.dzy.service.ReCommentFavourService;
import com.dzy.service.UserCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DZY
 * @description 针对表【re_comment_favour(评论点赞关联表(硬删除))】的数据库操作Service实现
 * @createDate 2024-04-03 17:31:34
 */
@Service
public class ReCommentFavourServiceImpl extends ServiceImpl<ReCommentFavourMapper, ReCommentFavour>
        implements ReCommentFavourService {

    @Autowired
    private UserCommentService userCommentService;

    /**
     * 点赞/取消点赞
     *
     * @param commentFavourAddRequest
     * @param loginUserVO
     * @return 点赞数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean doCommentFavour(CommentFavourAddRequest commentFavourAddRequest, UserLoginVO loginUserVO) {
        //校验
        Long cmtId = commentFavourAddRequest.getCmtId();
        if (cmtId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long userId = commentFavourAddRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //查询数据
        ReCommentFavour reCommentFavour = new ReCommentFavour();
        reCommentFavour.setCmtId(cmtId);
        reCommentFavour.setUserId(userId);
        QueryWrapper<ReCommentFavour> queryWrapper = new QueryWrapper<>(reCommentFavour);
        ReCommentFavour oldCommentFavour = this.getOne(queryWrapper);
        boolean result;
        //已点赞
        if (oldCommentFavour != null) {
            result = this.remove(queryWrapper);
            if (result) {
                // 点赞数 - 1
                result = userCommentService.update()
                        .eq("user_cmt_id", cmtId)
                        .gt("user_cmt_favour_count", 0)
                        .setSql("user_cmt_favour_count = user_cmt_favour_count - 1")
                        .update();
                return result;
            } else {
                throw new BusinessException(StatusCode.SYSTEM_ERROR);
            }
        } else {
            // 未点赞
            result = this.save(reCommentFavour);
            if (result) {
                // 点赞数 + 1
                result = userCommentService.update()
                        .eq("user_cmt_id", cmtId)
                        .setSql("user_cmt_favour_count = user_cmt_favour_count + 1")
                        .update();
                return result;
            } else {
                throw new BusinessException(StatusCode.SYSTEM_ERROR);
            }
        }
    }
}




