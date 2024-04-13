package com.dzy.model.dto.songlist;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/11  23:23
 */
@Data
public class DeleteBatchSongRequest implements Serializable {

    private static final long serialVersionUID = 7479168766983106229L;

    /**
     * 用户id（创建者id）
     */
    private Long userId;

    /**
     * 歌单id
     */
    private Long songlistId;

    /**
     * 歌曲id列表
     */
    private List<Long> songIdList;

}
