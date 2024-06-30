package com.dzy.model.dto.song;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/5/18  12:16
 */
@Data
public class SongDetailQueryRequest implements Serializable {

    private static final long serialVersionUID = -957680383357791312L;

    private Long songId;

}
