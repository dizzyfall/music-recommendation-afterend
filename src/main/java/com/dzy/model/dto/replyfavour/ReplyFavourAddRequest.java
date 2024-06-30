package com.dzy.model.dto.replyfavour;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/3  17:55
 */
@Data
public class ReplyFavourAddRequest implements Serializable {

    private static final long serialVersionUID = -7343504813604346036L;

    private Long replyId;

    private Long userId;

}
