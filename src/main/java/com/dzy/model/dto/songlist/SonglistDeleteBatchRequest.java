package com.dzy.model.dto.songlist;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/11  23:23
 */
@Data
public class SonglistDeleteBatchRequest implements Serializable {

    private static final long serialVersionUID = 9216069623260748757L;

    /**
     * 用户id（创建者id）
     */
    private Long userId;

    /**
     * 歌单id列表
     */
    private List<Long> songlistIdList;

}
