package com.dzy.model.dto.songlist;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/11  23:23
 */
@Data
public class SonglistCreateRequest implements Serializable {

    private static final long serialVersionUID = -7878118707303544013L;

    /**
     * 用户id（创建者id）
     */
    private Long userId;

    /**
     * 歌单名称
     */
    private String title;

}
