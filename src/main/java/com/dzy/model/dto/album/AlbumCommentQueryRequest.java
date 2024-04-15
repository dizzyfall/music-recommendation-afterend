package com.dzy.model.dto.album;

import com.dzy.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/8  22:38
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AlbumCommentQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = -109456041794707689L;

    /**
     * 专辑id
     */
    private Long albumId;

}
