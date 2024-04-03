package com.dzy.model.dto.singer;

import com.dzy.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/10  13:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SingerSearchTextQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 3330937688653421684L;

    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 歌手拼音
     */
    private String spell;

}
