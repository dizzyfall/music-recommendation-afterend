package com.dzy.model.dto.singer;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/10  13:51
 */
@Data
public class SingerQueryRequest implements Serializable {

    private static final long serialVersionUID = 7484284551459962418L;

    /**
     * 歌手Id
     */
    private Long singerId;

}
