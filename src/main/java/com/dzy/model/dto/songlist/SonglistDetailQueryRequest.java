package com.dzy.model.dto.songlist;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/7  15:09
 */
@Data
public class SonglistDetailQueryRequest implements Serializable {

    private static final long serialVersionUID = -8525221003250825021L;

    private Long songlistId;

}
