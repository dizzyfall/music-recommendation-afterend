package com.dzy.model.dto.comment;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/7  11:58
 */
@Data
public class CommentDeleteRequest implements Serializable {

    private static final long serialVersionUID = -6150948082058604338L;

    private Long userId;

    private Long commentId;

}
