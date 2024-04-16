package com.dzy.service;

import com.baomidou.mybatisplus.extension.service.IService;
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

}
