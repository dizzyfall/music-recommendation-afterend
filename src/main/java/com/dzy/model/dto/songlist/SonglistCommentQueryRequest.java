package com.dzy.model.dto.songlist;

import com.dzy.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/13  21:03
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SonglistCommentQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = -712336043420375544L;

    /**
     * 歌单id
     */
    private Long songlistId;

}
