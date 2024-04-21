package com.dzy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.model.entity.UserAuthority;

/**
 * @author DZY
 * @description 针对表【user_authority(用户权限表)】的数据库操作Service
 * @createDate 2024-04-21 17:02:37
 */
public interface UserAuthorityService extends IService<UserAuthority> {

    /**
     * 根据用户Id获取用户权限
     *
     * @param userId
     * @return com.dzy.model.entity.UserAuthority
     * @date 2024/4/21  17:05
     */
    UserAuthority getUserAuthorityById(Long userId);

}
