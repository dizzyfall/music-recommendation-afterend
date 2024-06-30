package com.dzy.model.vo.reply;

import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.entity.Reply;
import com.dzy.model.vo.userinfo.UserInfoIntroVO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/16  14:37
 */
@Data
public class ReplyVO implements Serializable {

    private static final long serialVersionUID = -1879532154981702119L;

    /**
     * 回复id
     */
    private Long replyId;

    /**
     * 接收回复者id
     */
    private Long receiverId;

    /**
     * 评论id(哪一个评论下回复)
     */
    private Long commentId;

    /**
     * 回复者信息简介
     */
    private UserInfoIntroVO userInfoIntroVO;

    /**
     * 接收回复的用户昵称
     */
    private String receiverNickName;

    /**
     * 回复内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Long favourCount;

    /**
     * 发布时间
     */
    private Date publishTime;

    public static ReplyVO objToVO(Reply reply) {
        if (reply == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        ReplyVO replyVO = new ReplyVO();
        try {
            BeanUtils.copyProperties(reply, replyVO);
        } catch (BusinessException e) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "Bean复制属性错误");
        }
        return replyVO;
    }

}
