package com.dzy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.model.dto.reply.MyReplyDeleteRequest;
import com.dzy.model.dto.reply.MyReplyQueryRequest;
import com.dzy.model.dto.reply.ReplyCreateRequest;
import com.dzy.model.dto.reply.ReplyQueryRequest;
import com.dzy.model.entity.Reply;
import com.dzy.model.vo.reply.ReplyVO;

/**
 * @author DZY
 * @description 针对表【reply(回复表)】的数据库操作Service
 * @createDate 2024-04-15 22:25:45
 */
public interface ReplyService extends IService<Reply> {

    /**
     * 获取ReplyVO视图对象
     *
     * @param reply
     * @return com.dzy.model.vo.reply.ReplyVO
     * @date 2024/4/16  15:41
     */
    ReplyVO getReplyVO(Reply reply);

    /**
     * 创建歌曲、专辑、歌单评论回复
     *
     * @param replyCreateRequest
     * @return java.lang.Boolean
     * @date 2024/4/16  23:31
     */
    Boolean createReply(ReplyCreateRequest replyCreateRequest);

    /**
     * 分页查询歌曲、专辑、歌单指定评论的回复
     *
     * @param replyQueryRequest
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.dzy.model.vo.reply.ReplyVO>
     * @date 2024/4/16  22:55
     */
    Page<ReplyVO> listReplyByPage(ReplyQueryRequest replyQueryRequest);

    /**
     * 分页查询自己的回复
     *
     * @param myReplyQueryRequest
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.dzy.model.vo.reply.ReplyVO>
     * @date 2024/4/18  10:55
     */
    Page<ReplyVO> listMyReplyByPage(MyReplyQueryRequest myReplyQueryRequest);

    /**
     * 删除自己的回复
     *
     * @param myReplyDeleteRequest
     * @return java.lang.Boolean
     * @date 2024/4/18  11:08
     */
    Boolean deleteMyReply(MyReplyDeleteRequest myReplyDeleteRequest);

}
