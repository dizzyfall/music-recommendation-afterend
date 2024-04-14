package com.dzy.model.dto.collect;

import lombok.Data;

import java.io.Serializable;

/**
 * 收藏专辑请求参数封装
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/10  9:31
 */
@Data
public class CollectSonglistRequest implements Serializable {

    private static final long serialVersionUID = 1480570298429454095L;

    private Long userId;

    private Long songlistId;

}
