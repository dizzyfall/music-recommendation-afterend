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
public class SingerTagsQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = -6315564133028878622L;

    /**
     * 歌手地区
     */
    private Integer area;

    /**
     * 歌手性别
     */
    private Integer sex;

    /**
     * 歌手类别
     */
    private Integer genre;

    /**
     * 歌手姓名第一个字母
     */
    private Integer nameSpellIndex;

}
