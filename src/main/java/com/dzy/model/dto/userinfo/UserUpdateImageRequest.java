package com.dzy.model.dto.userinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新请求参数封装
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/8  15:53
 */
@Data
public class UserUpdateImageRequest implements Serializable {

    private static final long serialVersionUID = 5021649555075714443L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户图片id
     */
    private Long imageId;

    /**
     * 用户图片类型
     */
    private String type;
}
