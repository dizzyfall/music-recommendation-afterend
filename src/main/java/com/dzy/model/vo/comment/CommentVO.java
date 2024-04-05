package com.dzy.model.vo.comment;

import com.dzy.model.vo.song.SongCommentVO;
import com.dzy.model.vo.userinfo.UserLoginVO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/5  10:09
 */
@Data
public class CommentVO implements Serializable {

    private static final long serialVersionUID = 7939516196351738858L;

    private UserLoginVO userLoginVO;

    private SongCommentVO songCommentVO;

    /**
     * 用户评论内容
     */
    private String content;

    /**
     * 用户评论点赞数量
     */
    private Long favourCount;

    /**
     * 用户评论发布时间
     */
    private Date publishTime;
}
