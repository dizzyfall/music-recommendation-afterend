package com.dzy.model.dto.reply;

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
public class MyReplyQueryRequest extends ReplyPageRequest implements Serializable {

    private static final long serialVersionUID = 8163453646494324094L;

    private Long userId;

}
