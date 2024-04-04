package com.dzy.model.dto.commentfavour;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/3  17:55
 */
@Data
public class CommentFavourAddRequest implements Serializable {

    private static final long serialVersionUID = 7117923009716196726L;

    private Long cmtId;

    private Long userId;

}
