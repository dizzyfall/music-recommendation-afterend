package com.dzy.model.dto.songlist;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/11  23:23
 */
@Data
public class SonglistDeleteRequest implements Serializable {

    private static final long serialVersionUID = 991353758120332907L;

    /**
     * 用户id（创建者id）
     */
    private Long userId;

    /**
     * 歌单id
     */
    private Long songlistId;

}
