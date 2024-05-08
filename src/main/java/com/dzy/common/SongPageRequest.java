package com.dzy.common;

import lombok.Data;

/**
 * 回复分页请求参数封装类
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/30  12:27
 */
@Data
public class SongPageRequest {

    /**
     * 当前页号
     */
    private int pageCurrent = 1;

    /**
     * 页面大小
     */
    private int pageSize = 20;

}
