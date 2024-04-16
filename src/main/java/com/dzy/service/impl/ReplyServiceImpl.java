package com.dzy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.ReplyMapper;
import com.dzy.model.entity.Reply;
import com.dzy.model.vo.reply.ReplyVO;
import com.dzy.model.vo.userinfo.UserInfoIntroVO;
import com.dzy.service.ReplyService;
import com.dzy.service.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

}




