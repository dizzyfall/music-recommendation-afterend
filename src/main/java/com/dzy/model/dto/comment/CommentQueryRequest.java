package com.dzy.model.dto.comment;

import com.dzy.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/5  9:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = -3869097273755713982L;

    private Long userId;

}
