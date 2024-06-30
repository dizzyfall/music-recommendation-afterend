package com.dzy.model.vo.singer;

import lombok.Data;

import java.io.Serializable;

/**
 * 歌手视图
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/10  11:49
 */
@Data
public class SingerIntroVO implements Serializable {

    private static final long serialVersionUID = -3946719007014582672L;

    /**
     * 歌手id
     */
    private Long singerId;

    /**
     * 歌手名字
     */
    private String singerName;

    /**
     * 歌手头像路径
     */
    private String avatarPath;

}
