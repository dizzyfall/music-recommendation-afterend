package com.dzy.model.dto.album;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/7  15:09
 */
@Data
public class AlbumSongQueryRequest implements Serializable {

    private static final long serialVersionUID = -1897904946366794619L;

    private Long singerId;

    private Long albumId;

}
