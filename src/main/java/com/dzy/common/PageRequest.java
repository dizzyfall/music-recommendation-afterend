package com.dzy.common;

import lombok.Data;

/**
 * 分页请求参数封装类
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/5  23:44
 */
@Data
public class PageRequest {

    /**
     * 当前页号
     */
    private int pageCurrent = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;
}
