package com.dzy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.model.dto.replyfavour.ReplyFavourAddRequest;
import com.dzy.model.entity.ReReplyFavour;

/**
 * @author DZY
 * @description 针对表【re_reply_favour(回复点赞关联表(硬删除))】的数据库操作Service
 * @createDate 2024-05-20 10:44:44
 */
public interface ReReplyFavourService extends IService<ReReplyFavour> {

    /**
     * 点赞/取消点赞
     *
     * @param replyFavourAddRequest
     * @return java.lang.Boolean
     * @date 2024/5/20  10:50
     */
    Boolean doReplyFavour(ReplyFavourAddRequest replyFavourAddRequest);

}
