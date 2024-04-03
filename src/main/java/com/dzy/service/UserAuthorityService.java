package com.dzy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.model.entity.UserAuthority;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户权限接口
 *
 * @author DZY
 * @description 针对表【user_authority(用户权限表)】的数据库操作Service
 * @createDate 2024-03-08 21:06:03
 */
public interface UserAuthorityService extends IService<UserAuthority> {

    /**
     * 用户是否为管理员
     *
     * @param request 请求域
     * @return boolean
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * /**
     * 用户是否为管理员
     *
     * @param userId 用户id
     * @return boolean
     */
    boolean isAdmin(Long userId);
}
