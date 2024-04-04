package com.dzy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.model.dto.commentfavour.CommentFavourAddRequest;
import com.dzy.model.entity.ReCommentFavour;
import com.dzy.model.vo.userinfo.UserLoginVO;

/**
 * @author DZY
 * @description 针对表【re_comment_favour(评论点赞关联表(硬删除))】的数据库操作Service
 * @createDate 2024-04-03 17:31:34
 */
public interface ReCommentFavourService extends IService<ReCommentFavour> {

    /**
     * 点赞/取消点赞
     *
     * @param commentFavourAddRequest
     * @param loginUserVO
     * @return 点赞数
     */
    Boolean doCommentFavour(CommentFavourAddRequest commentFavourAddRequest, UserLoginVO loginUserVO);

}
