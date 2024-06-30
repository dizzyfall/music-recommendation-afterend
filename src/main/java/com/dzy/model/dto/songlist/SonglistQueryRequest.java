package com.dzy.model.dto.songlist;

import com.dzy.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询收藏歌曲请求参数封装
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/10  9:31
 */
@Data
public class SonglistQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = -4531249775242227527L;

    private Long userId;

}
