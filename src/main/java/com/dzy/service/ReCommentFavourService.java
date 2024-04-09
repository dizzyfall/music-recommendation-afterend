package com.dzy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.model.dto.commentfavour.CommentFavourAddRequest;
import com.dzy.model.entity.ReCommentFavour;

/**
 * @author DZY
 * @description 针对表【re_comment_favour(评论点赞关联表(硬删除))】的数据库操作Service
 * @createDate 2024-04-05 11:25:09
 */
public interface ReCommentFavourService extends IService<ReCommentFavour> {

    /**
     * 点赞/取消点赞
     *
     * @param commentFavourAddRequest
     * @return 点赞数
     */
    Boolean doCommentFavour(CommentFavourAddRequest commentFavourAddRequest);

}
