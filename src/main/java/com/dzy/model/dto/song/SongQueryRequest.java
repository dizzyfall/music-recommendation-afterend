package com.dzy.model.dto.song;

import com.dzy.common.SongPageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/30  23:14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SongQueryRequest extends SongPageRequest implements Serializable {

    private static final long serialVersionUID = 8413006633062696184L;

    /**
     * 歌手Id
     */
    private Long singerId;

}
