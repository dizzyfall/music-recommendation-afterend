package com.dzy.model.dto.song;

import com.dzy.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/5  15:14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SongCommentQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 4891941397639685657L;

    private Long songId;

}
