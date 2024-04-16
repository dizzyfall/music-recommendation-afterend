package com.dzy.model.dto.song;

import com.dzy.common.ReplyPageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/3  12:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SongReplyQueryRequest extends ReplyPageRequest implements Serializable {

    private static final long serialVersionUID = -9071829285089595519L;

    /**
     * 评论id
     */
    private Long commentId;

}
