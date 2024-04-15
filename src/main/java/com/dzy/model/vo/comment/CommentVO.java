package com.dzy.model.vo.comment;

import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.entity.Comment;
import com.dzy.model.vo.userinfo.UserInfoIntroVO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论视图
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/5  10:09
 */
@Data
public class CommentVO implements Serializable {

    private static final long serialVersionUID = 7939516196351738858L;

    /**
     * 用户信息简介
     */
    private UserInfoIntroVO userInfoIntroVO;

    /**
     * 用户评论内容
     */
    private String content;

    /**
     * 用户评论点赞数量
     */
    private Long favourCount;

    /**
     * 评论回复数量
     */
    private Long replyCount;

    /**
     * 用户评论发布时间
     */
    private Date publishTime;

    public static CommentVO objToVO(Comment comment) {
        if (comment == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        CommentVO commentVO = new CommentVO();
        try {
            BeanUtils.copyProperties(comment, commentVO);
        } catch (BusinessException e) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "Bean复制属性错误");
        }
        return commentVO;
    }

}
