package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.ReReplyFavourMapper;
import com.dzy.model.dto.replyfavour.ReplyFavourAddRequest;
import com.dzy.model.entity.ReReplyFavour;
import com.dzy.service.ReReplyFavourService;
import com.dzy.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DZY
 * @description 针对表【re_reply_favour(回复点赞关联表(硬删除))】的数据库操作Service实现
 * @createDate 2024-05-20 10:44:44
 */
@Service
public class ReReplyFavourServiceImpl extends ServiceImpl<ReReplyFavourMapper, ReReplyFavour>
        implements ReReplyFavourService {

    @Autowired
    private ReplyService replyService;

    /**
     * 点赞/取消点赞
     *
     * @param replyFavourAddRequest
     * @return java.lang.Boolean
     * @date 2024/5/20  11:03
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean doReplyFavour(ReplyFavourAddRequest replyFavourAddRequest) {
        //校验
        Long replyId = replyFavourAddRequest.getReplyId();
        if (replyId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long userId = replyFavourAddRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //查询数据
        ReReplyFavour reReplyFavour = new ReReplyFavour();
        reReplyFavour.setReplyId(replyId);
        reReplyFavour.setUserId(userId);
        QueryWrapper<ReReplyFavour> queryWrapper = new QueryWrapper<>(reReplyFavour);
        ReReplyFavour oldReplyFavour = this.getOne(queryWrapper);
        boolean result;
        //已点赞
        if (oldReplyFavour != null) {
            //关联表移除数据
            result = this.remove(queryWrapper);
            if (result) {
                //点赞数-1
                result = replyService.update()
                        .eq("id", replyId)
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
            result = this.save(reReplyFavour);
            if (result) {
                //点赞数+1
                result = replyService.update()
                        .eq("id", replyId)
                        .setSql("favour_count = favour_count + 1")
                        .update();
                return result;
            } else {
                throw new BusinessException(StatusCode.SYSTEM_ERROR);
            }
        }
    }

}




