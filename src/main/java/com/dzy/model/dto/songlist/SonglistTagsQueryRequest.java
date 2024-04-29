package com.dzy.model.dto.songlist;

import com.dzy.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/29  20:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SonglistTagsQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = -1707143113690827835L;

    /**
     * 歌单语种
     */
    private Integer lang;

    /**
     * 歌单年代
     */
    private Integer era;

    /**
     * 歌单流派
     */
    private Integer genre;

    /**
     * 歌单场景
     */
    private Integer scene;

    /**
     * 歌单心情
     */
    private Integer mood;

    /**
     * 歌单主题
     */
    private Integer theme;

}
